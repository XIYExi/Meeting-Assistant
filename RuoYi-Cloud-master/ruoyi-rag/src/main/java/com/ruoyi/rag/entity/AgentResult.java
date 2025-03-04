package com.ruoyi.rag.entity;


import lombok.Data;

import java.util.Map;

@Data
public class AgentResult {

    private String model;

    private String type;

    private String form;

    private String name;

    private String timestamp;

    private String message_id;

    private String content;

    private Map<String, Object> results;



}
