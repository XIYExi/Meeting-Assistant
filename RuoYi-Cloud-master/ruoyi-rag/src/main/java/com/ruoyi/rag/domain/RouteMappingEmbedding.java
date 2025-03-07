package com.ruoyi.rag.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteMappingEmbedding {
    private Long id;
    private Long originId;
    private List<Float> embedding;
}
