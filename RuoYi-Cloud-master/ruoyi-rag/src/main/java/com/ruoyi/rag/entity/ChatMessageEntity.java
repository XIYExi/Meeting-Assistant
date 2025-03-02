package com.ruoyi.rag.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class ChatMessageEntity implements Serializable {
    private String type;
    private String text;

    public ChatMessageEntity(String type, String text) {
        this.text = text;
        this.type = type;
    }
}
