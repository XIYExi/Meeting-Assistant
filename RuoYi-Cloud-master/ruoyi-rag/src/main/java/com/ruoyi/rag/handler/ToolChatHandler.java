package com.ruoyi.rag.handler;

import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitEntity;
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
    public boolean handler(StepSplitEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
        // v2 同理，直接返回ok的output即可
        output.get(step).put("status", true);
        output.get(step).put("prompt", "");
        output.get(step).put("queryResult", "");
        output.get(step).put("routePath", "");
        output.get(step).put("intent", "chat");
        return true;
    }
}
