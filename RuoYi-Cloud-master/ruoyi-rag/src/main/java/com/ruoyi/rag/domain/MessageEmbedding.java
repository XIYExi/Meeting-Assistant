package com.ruoyi.rag.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEmbedding {
    // 会议id
    private Long id;
    // 存储数据库（标识什么类型）
    private String db;
    // emb向量
    private List<Float> embedding;
    // 标题
    private String title;
}
