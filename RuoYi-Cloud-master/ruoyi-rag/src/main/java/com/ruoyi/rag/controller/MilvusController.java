package com.ruoyi.rag.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.utils.IdGenerator;
import com.ruoyi.rag.utils.MilvusOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/milvus")
public class MilvusController {

    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private MilvusOperateUtils milvus;

    @GetMapping("/insert")
    public AjaxResult insert(@RequestParam("id") Long originalId, @RequestParam("dbType") Long dbType, @RequestParam("title") String title) {
        Response<List<Embedding>> listResponse = domesticEmbeddingModel.embedAll(Collections.singletonList(TextSegment.from(title)));
        List<Embedding> embeddings = listResponse.content();
        List<Float> features = embeddings.get(0).vectorAsList();
        long insert = milvus.insert(originalId, dbType, features);
        return AjaxResult.success(insert);
    }


    /**
     * 预留一个接口，主要用来测试，输入keyword（正常业务中是恒脑分割出来的），
     * 然后调用embedding计算之后search milvus获得最相似数据
     * @param keyword
     * @return
     */
    @Deprecated
    @GetMapping("/search")
    public AjaxResult search(String keyword) {
        Response<Embedding> listResponse = domesticEmbeddingModel.embed(keyword);
        Embedding embeddings = listResponse.content();
        List<Float> features = embeddings.vectorAsList();

        List<?> meeting = milvus.searchByFeature("meeting_home", features);

        List<Map<String, Long>> collect = meeting.stream().map(composeId -> {
            long originalId = IdGenerator.getOriginalId((Long) composeId);
            boolean isMeeting = IdGenerator.isMeeting((Long) composeId);
            Map<String, Long> parser = new HashMap<>();
            parser.put("id", originalId);
            parser.put("dbType", isMeeting ? 1L : 2L);
            return parser;
        }).collect(Collectors.toList());

        return AjaxResult.success(collect);
    }
}
