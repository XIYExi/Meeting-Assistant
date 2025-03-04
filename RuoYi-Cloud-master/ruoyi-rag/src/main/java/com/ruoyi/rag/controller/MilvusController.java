package com.ruoyi.rag.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
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
import java.util.List;

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
}
