package com.ruoyi.rag.assistant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResponse {
    private Long id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String location;
    private List<MeetingAgenda> agenda;
    private String url;
    private Long views;
    private Long type;
    private Long status;
    private Long meetingType;
    private String delFlag;

    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String remark;
}
