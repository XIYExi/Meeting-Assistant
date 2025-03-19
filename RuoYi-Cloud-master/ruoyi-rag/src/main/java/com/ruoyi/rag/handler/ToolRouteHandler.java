package com.ruoyi.rag.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.EmbeddingRouteMappingMilvus;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.domain.query.RouteMapping;
import com.ruoyi.rag.domain.StepSplitParamsFilterEntity;
import com.ruoyi.rag.domain.query.Meeting;
import com.ruoyi.rag.domain.query.News;
import com.ruoyi.rag.mapper.RouteMappingMapper;
import com.ruoyi.rag.mapper.query.MeetingGeoMapper;
import com.ruoyi.rag.mapper.query.MeetingMapper;
import com.ruoyi.rag.mapper.query.NewsMapper;
import com.ruoyi.rag.model.CustomPrompt;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
import com.ruoyi.rag.utils.RouteMappingOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Component
public class ToolRouteHandler implements ToolSimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(ToolRouteHandler.class);
    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private RouteMappingOperateUtils routeMilvus;
    @Resource
    private RouteMappingMapper routeMappingMapper;
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private NewsMapper newsMapper;
    @Resource
    private MeetingGeoMapper meetingGeoMapper;

    private WebSocketServerHandler nettyServerHandler = new WebSocketServerHandler();

    /**
     * v1 版本
     * @param content
     * @return
     */
    @Override
    public String handler(String content) {
        // 合并关键词
        String keywords = content.replace(", ", " ");
        Response<Embedding> embed = domesticEmbeddingModel.embed(TextSegment.textSegment(keywords));
        List<Float> keywordsEmbedding = embed.content().vectorAsList();

        List<?> router = routeMilvus.searchByFeature(EmbeddingRouteMappingMilvus.COLLECTION_NAME, keywordsEmbedding);
        Long routerId = (Long) router.get(0);

        RouteMapping routeMapping = routeMappingMapper.selectById(routerId);
        StringBuffer prompt = new StringBuffer("查询到最为匹配的路由信息是, ");
        prompt.append("页面信息page: ")
                .append(routeMapping.getPageType())
                .append(", 路径信息path: ")
                .append(routeMapping.getPath())
                .append("\t请给我一段描述，最后以: <view style=\"text-decoration: underline;font-size: 14px;color: #333;margin: 10px;\" onclick=\"handleRoute($0$)\">\n" +
                        "\t\t\t\t\t$1$\n" +
                        "\t\t\t\t</view>结尾，其中$0$地方填充路径信息path, $1$地方填充页面信息page");

        logger.info("Route Tool 处理完成");
        return prompt.toString();
    }


    /**
     * v2 版本
     * @param params
     * @param step
     * @param output 输出的结果，保存到map里面，保证每一步结果向下传递
     */
    @Override
    public boolean handler(StepSplitEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
        // 1. 先获取keywords，如果走的是route，那么肯定要先根据keywords去milvus里面查数据
        String keywords = params.getKeywords();
        Response<Embedding> embed = domesticEmbeddingModel.embed(TextSegment.textSegment(keywords));
        List<Float> keywordsEmbedding = embed.content().vectorAsList();
        List<?> router = routeMilvus.searchByFeature(EmbeddingRouteMappingMilvus.COLLECTION_NAME, keywordsEmbedding);
        Long routerId = (Long) router.get(0);
        RouteMapping routeMapping = routeMappingMapper.selectById(routerId);

        // 2. 判断是不是子页面 如果是子页面就需要检查 filters列表
        if (routeMapping.getLeaf() == 1) {
            // 2.1 filters不为空，说明提供了查询信息
            if (!params.getFilters().isEmpty()) {
                Long routerLeafId = this.generateQueryWrapperByFilters(routeMapping.getLeafDb(), params.getFilters());
                // 2.2 如果子页面id查出来是-1，表示数据库里面没查到，没这个页面，请求错误
                if (Objects.equals(routerLeafId, -1L)) {
                    String outputFailureFormatPrompt = String.format(CustomPrompt.ROUTE_FILTER_EMPTY_IN_LEAF, step, routeMapping.getPageType());
                    try {
                        nettyServerHandler.sendMsg(null, uid + "&^tool" + outputFailureFormatPrompt);
                    } catch (Exception e) {
                        logger.error("【route send failure msg through netty for intent ERROR!】 ",e.getMessage());
                    }
                    output.get(step).put("status", false);
                    output.get(step).put("prompt", outputFailureFormatPrompt);
                    output.get(step).put("queryResult", routeMapping);
                    output.get(step).put("routePath", "");
                    output.get(step).put("intent", "route");
                    return false;
                }
                // 2.3 子页面查询到了，继续执行拼接数据
                String finalRouterPath = routeMapping.getPath() + "?id=" + routerLeafId;
                String outputSuccessFormatPrompt = String.format(CustomPrompt.ROUTE_OUTPUT_PROMPT, routeMapping.getPageType(), routerLeafId, finalRouterPath);
                try {
                    nettyServerHandler.sendMsg(null, uid + "&^tool" + outputSuccessFormatPrompt);
                } catch (Exception e) {
                    logger.error("【route send success msg through netty for intent ERROR!】 ",e.getMessage());
                }
                output.get(step).put("status", true);
                output.get(step).put("prompt", outputSuccessFormatPrompt);
                output.get(step).put("queryResult", routeMapping);
                output.get(step).put("routePath", finalRouterPath);
                output.get(step).put("intent", "route");
            }
            // 2.2 filters为空，此时信息匹配错误，需要立刻掐断
            else {
                String outputFailureFormatPrompt = String.format(CustomPrompt.ROUTE_FILTER_EMPTY_IN_LEAF, step, routeMapping.getPageType());
                try {
                    nettyServerHandler.sendMsg(null, uid + "&^tool" + outputFailureFormatPrompt);
                } catch (Exception e) {
                    logger.error("【route send failure msg through netty for intent ERROR!】 ",e.getMessage());
                }
                output.get(step).put("status", false);
                output.get(step).put("prompt", outputFailureFormatPrompt);
                output.get(step).put("queryResult", routeMapping);
                output.get(step).put("routePath", "");
                output.get(step).put("intent", "route");
                return false;
            }
        }
        // 3. 不是子页面，直接匹配好了返回,此部分逻辑同 v1 版本
        else {
            String outputSuccessFormatPrompt = String.format(CustomPrompt.ROUTE_MAIN_OUTPUT_PROMPT, routeMapping.getPageType(), routeMapping.getPath());
            try {
                nettyServerHandler.sendMsg(null, uid + "&^tool" + outputSuccessFormatPrompt);
            } catch (Exception e) {
                logger.error("【route send success msg through netty for intent ERROR!】 ",e.getMessage());
            }
            output.get(step).put("status", true);
            output.get(step).put("prompt", outputSuccessFormatPrompt);
            output.get(step).put("queryResult", routeMapping);
            output.get(step).put("routePath", routeMapping.getPath());
            output.get(step).put("intent", "route");
            logger.info("Route Tool 处理完成");
        }

        return true;
    }


    /**
     * 根据db，filters条件封装对应数据库的QueryWrapper
     * @param db
     * @param filters
     * @return 对应需要跳转子页面的id
     */
    private Long generateQueryWrapperByFilters(
            String db,
            List<StepSplitParamsFilterEntity> filters
    ) {
        switch (db) {
            case "meeting":
                QueryWrapper<Meeting> meetingQueryWrapper= new QueryWrapper<>();
                for (StepSplitParamsFilterEntity filter : filters) {
                    if (filter.getOperator().equals("eq"))
                        meetingQueryWrapper.eq(filter.getFilter(), filter.getValue());
                    else if (filter.getOperator().equals("like"))
                        meetingQueryWrapper.like(filter.getFilter(), filter.getValue());

                    if ("desc".equals(filter.getOrder()))
                        meetingQueryWrapper.orderByDesc(filter.getFilter());
                    else if ("asc".equals(filter.getOrder()))
                        meetingQueryWrapper.orderByAsc(filter.getFilter());
                }
                meetingQueryWrapper.last("limit 1");
                List<Meeting> meetings = meetingMapper.selectList(meetingQueryWrapper);
                if (meetings.isEmpty())
                    return -1L;
                Meeting meeting = meetings.get(0);
                // 路由子页面的id
                Long routerLeafId = meeting.getId();
                return routerLeafId;
            case "news":
                QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
                for (StepSplitParamsFilterEntity filter : filters) {
                    if (filter.getOperator().equals("eq"))
                        newsQueryWrapper.eq(filter.getFilter(), filter.getValue());
                    else if (filter.getOperator().equals("like"))
                        newsQueryWrapper.like(filter.getFilter(), filter.getValue());

                    if ("desc".equals(filter.getOrder()))
                        newsQueryWrapper.orderByDesc(filter.getFilter());
                    else if ("asc".equals(filter.getOrder()))
                        newsQueryWrapper.orderByAsc(filter.getFilter());
                }
                newsQueryWrapper.last("limit 1");
                List<News> news = newsMapper.selectList(newsQueryWrapper);
                if (news.isEmpty())
                    return -1L;
                News _new = news.get(0);
                Long newsRouterLeadIf = _new.getId();
                return newsRouterLeadIf;
            case "meeting_geo":
                QueryWrapper<Meeting> geoQueryWrapper = new QueryWrapper<>();
                for (StepSplitParamsFilterEntity filter : filters) {
                    if (filter.getOperator().equals("eq"))
                        geoQueryWrapper.eq(filter.getFilter(), filter.getValue());
                    else if (filter.getOperator().equals("like"))
                        geoQueryWrapper.like(filter.getFilter(), filter.getValue());

                    if ("desc".equals(filter.getOrder()))
                        geoQueryWrapper.orderByDesc(filter.getFilter());
                    else if ("asc".equals(filter.getOrder()))
                        geoQueryWrapper.orderByAsc(filter.getFilter());
                }
                geoQueryWrapper.last("limit 1");
                List<Meeting> meetingGeos = meetingMapper.selectList(geoQueryWrapper);
                if (meetingGeos.isEmpty()) return -1L;
                Meeting meetingTemp = meetingGeos.get(0);
                long geoId = Long.parseLong(meetingTemp.getLocation());
                return geoId;
            default:
                break;
        }
        return -1L;
    }
}
