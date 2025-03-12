package com.ruoyi.rag.handler;

import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * v2版本 用来替代v1版本的action操作，执行数据库查询操作
 */
@Component
public class ToolQueryHandler implements ToolSimpleHandler {
    @Override
    public String handler(String content) {
        return "";
    }

    @Override
    public boolean handler(StepSplitParamsEntity params, int step, Map<Integer, Map<String, Object>> output, String uid) {

        return false;
    }
}
