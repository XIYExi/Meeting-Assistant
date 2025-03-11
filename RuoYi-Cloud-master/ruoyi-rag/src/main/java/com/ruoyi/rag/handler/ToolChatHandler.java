package com.ruoyi.rag.handler;

import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ToolChatHandler implements ToolSimpleHandler {
    @Override
    public String handler(String content) {
        // chat 目前直接pass，不需要封装system prompt，直接走agent在查询一次
        return "";
    }

    @Override
    public void handler(StepSplitParamsEntity params, Map<Integer, Map<String, Object>> output) {

    }
}
