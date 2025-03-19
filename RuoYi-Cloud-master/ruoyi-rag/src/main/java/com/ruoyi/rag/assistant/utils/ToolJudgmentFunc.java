package com.ruoyi.rag.assistant.utils;

import com.ruoyi.rag.assistant.entity.Filter;
import com.ruoyi.rag.assistant.entity.StepDefinition;

public class ToolJudgmentFunc {

    public static boolean isVectorSearchRequired(StepDefinition step, Filter filter) {
        return "title".equals(filter.getField()) && "meeting".equals(step.getDb());
    }

}
