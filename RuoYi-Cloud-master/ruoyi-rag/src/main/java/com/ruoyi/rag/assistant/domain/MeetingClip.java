package com.ruoyi.rag.assistant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("meeting_clip")
public class MeetingClip implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long meetingId;
    private Long agendaId;
    private Integer clipType;
    private String fileName;
    private Long uploadUserId;
    private String fileType;
    private Integer fileSize;
    private String url;

    private String delFlag;
    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
