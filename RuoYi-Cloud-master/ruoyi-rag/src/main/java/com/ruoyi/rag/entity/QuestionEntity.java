package com.ruoyi.rag.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionEntity implements Serializable {
    // 会话ID
    private String sid;
    // 查询ID
    private String qid;
    // ⽤户ID
    private String uid;
    // 查询意图
    private String intent;
    // 回答消息
    private MessageEntity message;
    // 错误信息
    private String errorMessage;
    // 状态
    private String status;
    // 请求ID
    private String requestId;
}
