package com.ruoyi.rag.assistant.entity;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Filter implements Serializable {
    private String field;
    private String operator;
    private Object value;
}
