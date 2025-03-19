package com.ruoyi.rag.assistant.declare;

import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.domain.StepSplitParamsEntity;

import java.util.Map;

public interface EnhancedToolHandler {
    boolean handler(StepDefinition step, QueryContext context);
}
