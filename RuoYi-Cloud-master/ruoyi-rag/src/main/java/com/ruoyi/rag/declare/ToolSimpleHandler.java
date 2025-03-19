package com.ruoyi.rag.declare;

import com.ruoyi.rag.domain.StepSplitEntity;

import java.util.Map;

public interface ToolSimpleHandler {

    String handler(String content);

    boolean handler(StepSplitEntity params, int step, Map<Integer, Map<String, Object>> output, String uid);
}
