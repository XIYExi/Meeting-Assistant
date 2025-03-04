package com.ruoyi.rag.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * agent聊天记录对象 messages_logs
 * 
 * @author xiye
 * @date 2025-01-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("messages_logs")
@ToString
public class MessagesLogs implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private String userId;
    private String role;
    private String content;
    private String delFlag;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String remark;
}
