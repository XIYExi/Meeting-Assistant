package com.ruoyi.rag.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageEntity implements Serializable {
    private String role;
    private String content;
    private Integer seq;
    private String type;
    private String encoding;
    private List<String> files;
}
