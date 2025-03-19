package com.ruoyi.rag.assistant.handler;

import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EnhancedChatToolHandler implements EnhancedToolHandler {
    @Override
    public boolean handler(StepDefinition step, QueryContext context) {
        return false;
    }
}
