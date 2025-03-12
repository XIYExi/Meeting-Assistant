package com.ruoyi.rag.handler;

import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ToolReservationHandler implements ToolSimpleHandler {
    @Override
    public String handler(String content) {
        return "";
    }

    @Override
    public boolean handler(StepSplitParamsEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {
        return false;
    }
}
