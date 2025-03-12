package com.ruoyi.rag.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.EmbeddingRouteMappingMilvus;
import com.ruoyi.rag.domain.RouteMapping;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import com.ruoyi.rag.domain.StepSplitParamsFilterEntity;
import com.ruoyi.rag.domain.query.Meeting;
import com.ruoyi.rag.domain.query.MeetingGeo;
import com.ruoyi.rag.domain.query.News;
import com.ruoyi.rag.mapper.RouteMappingMapper;
import com.ruoyi.rag.mapper.query.MeetingGeoMapper;
import com.ruoyi.rag.mapper.query.MeetingMapper;
import com.ruoyi.rag.mapper.query.NewsMapper;
import com.ruoyi.rag.model.CustomPrompt;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.utils.RouteMappingOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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
    public boolean handler(StepSplitParamsEntity params, int step, Map<Integer, Map<String, Object>> output) {
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
                String finalRouterPath = routeMapping.getPath() + "?id=" + routerLeafId;
                String outputSuccessFormatPrompt = String.format(CustomPrompt.ROUTE_OUTPUT_PROMPT, routeMapping.getPageType(), routerLeafId, finalRouterPath);
                output.get(step).put("status", true);
                output.get(step).put("prompt", outputSuccessFormatPrompt);
                output.get(step).put("queryResult", routeMapping);
                output.get(step).put("intent", "route");
            }
            // 2.2 filters为空，此时信息匹配错误，需要立刻掐断
            else {
                String outputFailureFormatPrompt = String.format(CustomPrompt.ROUTE_FILTER_EMPTY_IN_LEAF, routeMapping.getPageType());
                output.get(step).put("status", false);
                output.get(step).put("prompt", outputFailureFormatPrompt);
                output.get(step).put("queryResult", routeMapping);
                output.get(step).put("intent", "route");
                return false;
            }
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
                meetingQueryWrapper.like(filters.get(0).getFilter(), filters.get(0).getValue());
                meetingQueryWrapper.last("limit 1");
                Meeting meeting = meetingMapper.selectList(meetingQueryWrapper).get(0);
                // 路由子页面的id
                Long routerLeafId = meeting.getId();
                return routerLeafId;
            case "news":
                QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
                newsQueryWrapper.like(filters.get(0).getFilter(), filters.get(0).getValue());
                newsQueryWrapper.last("limit 1");
                News news = newsMapper.selectList(newsQueryWrapper).get(0);
                Long newsRouterLeadIf = news.getId();
                return newsRouterLeadIf;
            case "meeting_geo":
                QueryWrapper<MeetingGeo> geoQueryWrapper = new QueryWrapper<>();
                geoQueryWrapper.like(filters.get(0).getFilter(), filters.get(0).getValue());
                geoQueryWrapper.last("limit 1");
                MeetingGeo meetingGeo = meetingGeoMapper.selectList(geoQueryWrapper).get(0);
                Long geoRouterLeadIf = meetingGeo.getId();
                return geoRouterLeadIf;
            default:
                break;
        }
        return 1L;
    }
}
