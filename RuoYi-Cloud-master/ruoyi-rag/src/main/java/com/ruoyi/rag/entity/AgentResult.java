package com.ruoyi.rag.entity;


import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class AgentResult {

    private String model;

    private String type;

    private String from;

    private String name;

    private String timestamp;

    private String message_id;

    private String content;

    private Map<String, Object> results;



}
