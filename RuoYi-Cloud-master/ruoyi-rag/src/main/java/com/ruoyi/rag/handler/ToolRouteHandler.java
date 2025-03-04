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



@Component
public class ToolRouteHandler implements ToolSimpleHandler {

    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private MilvusOperateUtils milvus;

    @Override
    public String handler(String content) {
        return "";
    }
}
