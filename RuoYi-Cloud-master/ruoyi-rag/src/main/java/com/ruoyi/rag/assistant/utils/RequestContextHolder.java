package com.ruoyi.rag.assistant.utils;

import com.ruoyi.rag.assistant.entity.StepDefinition;

import java.util.List;

public class RequestContextHolder {
    private static final ThreadLocal<List<StepDefinition>> requestList = new ThreadLocal<>();

     // 设置 List 集合
    public static void setRequestList(List<StepDefinition> list) {
        requestList.set(list);
    }

    // 获取 List 集合
    public static List<StepDefinition> getRequestList() {
        return requestList.get();
    }

    // 清除 ThreadLocal 中的数据
    public static void clear() {
        requestList.remove();
    }
}
