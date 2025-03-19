package com.ruoyi.rag.assistant.declare;

import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;

public interface QueryProcessor {
    public void processor(StepDefinition step, QueryContext context);
}
