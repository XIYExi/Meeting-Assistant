package com.ruoyi.rag.assistant.entity;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StepDefinition {
    private Integer step;
    private String intent;
    private String subtype;
    private String db;
    private Integer dependency;
    private Map<String, String> dataBindings;
    private List<Filter> filters;
    private Map<String, String> params;
    private Map<String, String> authCondition;
    private List<String> outputFields;
}