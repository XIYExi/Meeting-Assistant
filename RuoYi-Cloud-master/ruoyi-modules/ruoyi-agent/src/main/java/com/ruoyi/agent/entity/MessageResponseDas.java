package com.ruoyi.agent.entity;

import lombok.Data;

@Data
public class MessageResponseDas {
    private String code;
    private String msg;
    private AgentResult data;
}
