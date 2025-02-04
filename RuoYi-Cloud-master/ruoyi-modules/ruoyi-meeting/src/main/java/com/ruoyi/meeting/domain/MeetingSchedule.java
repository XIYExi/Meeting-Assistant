package com.ruoyi.meeting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
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


    private String phone;

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


}
