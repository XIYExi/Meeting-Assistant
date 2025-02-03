package com.ruoyi.meeting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 会议预约对象 meeting_schedule
 * 
 * @author xiye
 * @date 2025-02-03
 */
public class MeetingSchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会议预约表id */
    private Long id;

    /** 会议id */
    @Excel(name = "会议id")
    private Long meetingId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 会议名称 */
    @Excel(name = "会议名称")
    private String title;

    /** 会议开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会议开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /** 会议结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会议结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMeetingId(Long meetingId) 
    {
        this.meetingId = meetingId;
    }

    public Long getMeetingId() 
    {
        return meetingId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setBeginTime(Date beginTime) 
    {
        this.beginTime = beginTime;
    }

    public Date getBeginTime() 
    {
        return beginTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("meetingId", getMeetingId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
