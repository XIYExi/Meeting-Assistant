package com.ruoyi.rag.handler;

import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.EmbeddingRouteMappingMilvus;
import com.ruoyi.rag.domain.RouteMapping;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import com.ruoyi.rag.mapper.RouteMappingMapper;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.utils.IdGenerator;
import com.ruoyi.rag.utils.MilvusOperateUtils;
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
import java.util.stream.Collectors;



@Component
public class ToolRouteHandler implements ToolSimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(ToolRouteHandler.class);
    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private RouteMappingOperateUtils routeMilvus;
    @Resource
    private RouteMappingMapper routeMappingMapper;

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

    @Override
    public void handler(StepSplitParamsEntity params, Map<Integer, Map<String, Object>> output) {

    }
}
