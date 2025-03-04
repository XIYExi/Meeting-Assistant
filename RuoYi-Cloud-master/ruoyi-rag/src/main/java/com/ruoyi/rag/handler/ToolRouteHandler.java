package com.ruoyi.rag.handler;

import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.utils.IdGenerator;
import com.ruoyi.rag.utils.MilvusOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.output.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 工具链：【Action操作】
 * 此操作目的是让Model可以接入数据库，统计算计Embedding到Milvus获取匹配的项目，并通过解析id和type获取对应的数据并返回
 *
 * 流程如下：
 * 1. 调用EmbeddingMode计算embedding -> 2. Milvus向量库相似度计算 -> 3. 选择最为匹配的数据，解析id和dbType（来自哪个数据库） ->
 * 4. feign调用ruoyi-meeting获取对应的数据 -> 5. 获取之后调用ws发送数据，此阶段打上“thinking”标志 -> 6. 最后整合，再送一次大模型总结输入最后结果
 */
@Component
public class ToolRouteHandler implements ToolSimpleHandler {

    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private MilvusOperateUtils milvus;

    @Override
    public void handler(String content) {
        // TODO content到底是什么格式呢？先按单个字符算吧
        Response<Embedding> listResponse = domesticEmbeddingModel.embed(content);
        Embedding embeddings = listResponse.content();
        List<Float> features = embeddings.vectorAsList();

        List<?> meeting = milvus.searchByFeature("meeting_home", features);

        for (int i = 0; i < 2; ++i) {
            Long composeId = (Long)meeting.get(i);
            long originalId = IdGenerator.getOriginalId((Long) composeId);
            boolean isMeeting = IdGenerator.isMeeting((Long) composeId);
            int dbType = isMeeting ? 1 : 2;

            // 远程meeting 获取信息

        }

    }
}
