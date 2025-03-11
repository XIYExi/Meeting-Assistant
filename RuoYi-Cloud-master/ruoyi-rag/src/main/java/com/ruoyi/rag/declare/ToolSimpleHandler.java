package com.ruoyi.rag.declare;

import com.ruoyi.rag.domain.StepSplitParamsEntity;

import java.util.Map;

public interface ToolSimpleHandler {

    String handler(String content);

    void handler(StepSplitParamsEntity params, Map<Integer, Map<String, Object>> output);
}
