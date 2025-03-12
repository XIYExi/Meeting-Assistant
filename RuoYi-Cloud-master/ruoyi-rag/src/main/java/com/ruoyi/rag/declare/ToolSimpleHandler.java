package com.ruoyi.rag.declare;

import com.ruoyi.rag.domain.StepSplitParamsEntity;

import java.util.Map;

public interface ToolSimpleHandler {

    String handler(String content);

    boolean handler(StepSplitParamsEntity params, int step,  Map<Integer, Map<String, Object>> output, String uid);
}
