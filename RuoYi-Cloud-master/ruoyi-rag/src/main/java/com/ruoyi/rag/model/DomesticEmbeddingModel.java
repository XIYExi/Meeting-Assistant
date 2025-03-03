package com.ruoyi.rag.model;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.rag.config.RagParamConfig;
import com.ruoyi.rag.config.RagRequestPath;
import com.ruoyi.rag.entity.EmbeddingEntity;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DomesticEmbeddingModel implements EmbeddingModel {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RagParamConfig ragParamConfig;

    @Override
    public Response<List<Embedding>> embedAll(List<TextSegment> list) {
        String executeUrl = RagRequestPath.qwenTextEmbeddingExecuteUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + ragParamConfig.getQwenAppKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", ragParamConfig.getQwenTextEmbedding());
        requestBody.put("input", list.get(0).text());
        requestBody.put("dimensions", ragParamConfig.getQwenTextDimensions());
        requestBody.put("encoding_format", "float");


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(executeUrl, requestEntity, Map.class);

        Map result = responseEntity.getBody();
        List<EmbeddingEntity> embeddingList = JSONArray.parseArray(JSONArray.toJSONString(result.get("data")), EmbeddingEntity.class);
        List<Embedding> collect = embeddingList.stream().map(embeddingItem -> {
            Embedding embedding = Embedding.from(embeddingItem.getEmbedding());
            return embedding;
        }).collect(Collectors.toList());
        return Response.from(collect);
    }

    @Override
    public Response<Embedding> embed(String text) {
        TextSegment from = TextSegment.from(text);
        Response<List<Embedding>> listResponse = embedAll(Collections.singletonList(from));
        List<Embedding> content = listResponse.content();
        return Response.from(content.get(0));
    }

    @Override
    public int dimension() {
        return 1024; // 预设qwen输入的维度是1024！
    }
}
