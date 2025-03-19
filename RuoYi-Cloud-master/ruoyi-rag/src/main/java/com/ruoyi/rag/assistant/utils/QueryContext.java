package com.ruoyi.rag.assistant.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 核心上下文管理器
 */
public class QueryContext {
    private final Map<String, Object> stepData = new ConcurrentHashMap<>();
    private final Pattern BINDING_PATTERN = Pattern.compile("step(\\d+)\\.(\\w+)");


    // 存储步骤结果
    public void storeStepResult(int stepNumber, Object result) {
        stepData.put("step" + stepNumber, result);
    }

    // 获取绑定值
    public Object resolveBinding(String expression) {
        Matcher matcher = BINDING_PATTERN.matcher(expression);
        if (matcher.find()) {
            int step = Integer.parseInt(matcher.group(1));
            String field = matcher.group(2);
            return getFieldValue(step, field);
        }
        return expression;
    }

    public boolean containsBinding(String expression) {
        Matcher matcher = BINDING_PATTERN.matcher(expression);
        if (matcher.find())
            return true;
        return false;
    }


    private Object getFieldValue(int step, String field) {
        Object data = stepData.get("step" + step);
        if (data instanceof Map) {
            return ((Map<?, ?>) data).get(field);
        }
        // 反射获取字段值
        try {
            Field declaredField = data.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(data);
        } catch (Exception e) {
            throw new RuntimeException("Field resolution failed", e);
        }
    }


    public boolean containsStep(String dependencyKey) {
        return stepData.containsKey(dependencyKey);
    }

}
