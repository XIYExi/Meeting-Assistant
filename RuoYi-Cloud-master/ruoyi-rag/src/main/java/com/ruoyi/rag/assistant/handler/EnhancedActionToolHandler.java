package com.ruoyi.rag.assistant.handler;

import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import org.springframework.stereotype.Component;


@Component
public class EnhancedActionToolHandler implements EnhancedToolHandler {
    @Override
    public boolean handler(StepDefinition step, QueryContext context) {
        return false;
    }
}
