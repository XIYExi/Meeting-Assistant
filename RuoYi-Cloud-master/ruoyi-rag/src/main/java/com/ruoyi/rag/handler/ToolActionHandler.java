package com.ruoyi.rag.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.RemoteScheduleService;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.utils.IdGenerator;
import com.ruoyi.rag.utils.MilvusOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 工具链：【Action操作】
 * 此操作目的是让Model可以接入数据库，统计算计Embedding到Milvus获取匹配的项目，并通过解析id和type获取对应的数据并返回
 *
 * 流程如下：
 * 1. 调用EmbeddingMode计算embedding -> 2. Milvus向量库相似度计算 -> 3. 选择最为匹配的数据，解析id和dbType（来自哪个数据库） ->
 * 4. feign调用ruoyi-meeting获取对应的数据 -> 5. 获取之后调用ws发送数据，此阶段打上“thinking”标志 -> 6. 最后整合，再送一次大模型总结输入最后结果
 */
@Component
public class ToolActionHandler implements ToolSimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(ToolActionHandler.class);
    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private MilvusOperateUtils milvus;
    @Resource
    private RemoteScheduleService remoteMeetingService;

    @Override
    public String handler(String content) {
        StringBuffer buffer = new StringBuffer();
        String[] split = content.split(", ");
        for (int i=0; i<split.length; i++) {
            // TODO content到底是什么格式呢？先按单个字符算吧
            Response<Embedding> listResponse = domesticEmbeddingModel.embed(split[i]);
            Embedding embeddings = listResponse.content();
            List<Float> features = embeddings.vectorAsList();
            List<?> meeting = milvus.searchByFeature("meeting_home", features);

            buffer.append("关键词【").append(content).append("】最匹配的内容为：\n");
            Long composeId = (Long)meeting.get(0);
            long originalId = IdGenerator.getOriginalId((Long) composeId);
            boolean isMeeting = IdGenerator.isMeeting((Long) composeId);
            long dbType = isMeeting ? 1L : 2L;

            // 远程meeting 获取信息
            AjaxResult messageByAgenda = remoteMeetingService.getMessageByAgenda(originalId, dbType);
            Map message = JSON.parseObject(JSONObject.toJSONString(messageByAgenda.get("data")), Map.class);
            if (dbType == 1L) {
                // 解析会议
                buffer.append("\t\t会议标题为【").append(message.get("title")).append("】，开始时间为【").append(message.get("beginTime")).append("】,结束时间为【")
                        .append(message.get("endTime")).append("】，会议介绍为：【").append(message.get("content")).append("】\n");
            }
            else {
                // 解析子议程
                buffer.append("\t\t子议程为【").append(message.get("title")).append("】，开始时间为【").append(message.get("beginTime")).append("】,结束时间为【")
                        .append(message.get("endTime")).append("】，子议程介绍为：【").append(message.get("content")).append("】,其余信息为：【")
                        .append(message.get("meta")).append("】\n");
            }
        }
        // 新的提示词
        String prompt = buffer.toString();
        System.err.println(prompt);
        logger.info("Action Tool 处理完成");
        return prompt;
    }
}
