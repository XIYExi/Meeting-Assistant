package com.ruoyi.rag.assistant.handler.route;

import com.ruoyi.rag.assistant.component.VectorSearchComponent;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.domain.Meeting;
import com.ruoyi.rag.assistant.domain.RouteMapping;
import com.ruoyi.rag.assistant.entity.MeetingResponse;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.mapper.MeetingGeoMapper;
import com.ruoyi.rag.assistant.mapper.MeetingMapper;
import com.ruoyi.rag.assistant.mapper.NewsMapper;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.domain.EmbeddingMilvus;
import com.ruoyi.rag.domain.EmbeddingRouteMappingMilvus;
import com.ruoyi.rag.handler.ToolRouteHandler;
import com.ruoyi.rag.mapper.RouteMappingMapper;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
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

@Component
public class RoutePageProcessor implements QueryProcessor {
    private static final Logger logger = LoggerFactory.getLogger(RoutePageProcessor.class);
    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private RouteMappingOperateUtils routeMilvus;
    @Resource
    private RouteMappingMapper routeMappingMapper;
    @Resource
    private VectorSearchComponent vectorSearchComponent;

    @Override
    public void processor(StepDefinition step, QueryContext context) {
        String queryKeywords = step.getParams().get("query");
        Response<Embedding> embed = domesticEmbeddingModel.embed(TextSegment.textSegment(queryKeywords));
        List<Float> keywordsEmbedding = embed.content().vectorAsList();

        List<?> router = routeMilvus.searchByFeature(EmbeddingRouteMappingMilvus.COLLECTION_NAME, keywordsEmbedding);
        long routerId = (Long) router.get(0);
        RouteMapping routeMapping = routeMappingMapper.selectById(routerId);

        Map<String, Object> resultItem = new HashMap<>();
        // 判断是不是子页面 如果是子页面就需要检查meeting找到匹配的会议id
        if (routeMapping.getLeaf() == 1) {
            MeetingResponse meetingResponse = vectorSearchComponent.vectorSearch(queryKeywords);

            resultItem.put("path", routeMapping.getPath() + "?id=" + meetingResponse.getId());
            resultItem.put("title", routeMapping.getPageType());
        }
        // 3. 不是子页面，直接匹配好了返回
        else {
            resultItem.put("path", routeMapping.getPath());
            resultItem.put("title", routeMapping.getPageType());
        }
        context.storeStepResult(step.getStep(), resultItem);
    }
}
