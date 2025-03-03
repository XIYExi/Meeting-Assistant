package com.ruoyi.rag.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddingEntity {
    private List<Float> embedding;
    private Integer index;
    private String object;
}
