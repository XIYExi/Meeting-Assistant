package com.ruoyi.rag.assistant.declare;

import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;

public interface QuerySubHandler {
    public void handler(StepDefinition step, QueryContext context);
}
