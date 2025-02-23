package com.ruoyi.meeting.entity;

import com.ruoyi.meeting.domain.MeetingGeo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResponse {

    private Long id;
    private String title;
    private Date beginTime;
    private Date endTime;
    private MeetingGeo location;
    private String url;
    private Long views;
    private Long type;
    private Long status;
    private Long meetingType;
    private String delFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String remark;


}
