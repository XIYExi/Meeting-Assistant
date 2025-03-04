package com.ruoyi.meeting.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 送到 rag 里面计算 embedding 并存到 Milvus 里面的数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingMilvusEntity {
    private Long id;
    private Long dbType;
    private String title;
}
