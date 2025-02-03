package com.ruoyi.meeting.service;

import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.meeting.constant.MeetingConstant;
import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.domain.MeetingSchedule;
import com.ruoyi.meeting.mapper.MeetingMapper;
import com.ruoyi.meeting.mapper.MeetingScheduleMapper;
import com.ruoyi.meeting.qo.MeetingReservationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 会议 事务表
 * 用来定时任务提醒会议开始，定时修改会议状态等
 */
@Service
public class IRecordScheduleService {

    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private MeetingScheduleMapper meetingScheduleMapper;


    public String MeetingReservation(MeetingReservationQuery meetingReservationQuery) {
        if (!Objects.equals(meetingReservationQuery.getStatus(), MeetingConstant.MEETING_STATUE_NOT_YET_STARTED)) {
            return MeetingConstant.RETURN_IS_OPENING;
        }


        // Meeting meetingMessage = meetingMapper.selectMeetingById(meetingReservationQuery.getId());
        // 添加预约信息
        Date meetingBeginTime = meetingReservationQuery.getBeginTime();
        // 一天之前
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(meetingBeginTime);
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();

        // quartz定时任务 提前一天发送短信通知


        // 添加预约信息
        MeetingSchedule meetingSchedule = new MeetingSchedule();
        meetingSchedule.setMeetingId(meetingReservationQuery.getId());
        meetingSchedule.setUserId(meetingReservationQuery.getUserId());
        meetingSchedule.setBeginTime(meetingReservationQuery.getBeginTime());
        meetingSchedule.setEndTime(meetingReservationQuery.getEndTime());
        meetingSchedule.setTitle(meetingReservationQuery.getTitle());
        meetingSchedule.setCreateTime(DateUtils.getNowDate());
        meetingScheduleMapper.insertMeetingSchedule(meetingSchedule);

        return "";
    }


}
