package com.ruoyi.meeting.qo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingReservationQuery {
    /** 会议表id */
    private Long id;

    /** 当前用户id */
    private Long userId;

    /** 会议名称 */
    private String title;

    private String phone;

    /** 会议开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** 会议结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 会议表状态 */
    private Long status;
}
