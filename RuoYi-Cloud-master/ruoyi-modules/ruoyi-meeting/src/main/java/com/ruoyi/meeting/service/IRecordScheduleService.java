package com.ruoyi.meeting.service;

import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.meeting.constant.MeetingConstant;
import com.ruoyi.meeting.domain.MeetingSchedule;
import com.ruoyi.meeting.mapper.MeetingScheduleMapper;
import com.ruoyi.meeting.qo.MeetingReservationQuery;
import com.ruoyi.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 会议 事务表
 * 用来定时任务提醒会议开始，定时修改会议状态等
 */
@Service
public class IRecordScheduleService {


    @Autowired
    private MeetingScheduleMapper meetingScheduleMapper;


    /**
     * 取消预约
     * @param id
     * @return
     */
    public boolean MeetingPartRemove(Long id) {
        int i = meetingScheduleMapper.deleteMeetingScheduleById(id);
        return i == 1;
    }


    /**
     * 出席会议的用户列表
     * @return
     */
    public List<SysUser> MeetingPartList(Long meetingId) {
        List<SysUser> sysUsers = meetingScheduleMapper.selectPartUserList(meetingId);
        return sysUsers;
    }



    /**
     * 提交会议预约，此时只需要添加record到数据库即可。
     * 系统会在每天晚上8点的时候扫描会议列表，然后发送信息。
     * @param meetingReservationQuery
     * @return
     */
    public boolean MeetingReservation(MeetingReservationQuery meetingReservationQuery) {
        if (!Objects.equals(meetingReservationQuery.getStatus(), MeetingConstant.MEETING_STATUE_NOT_YET_STARTED)) {
            return false;
        }

        // 添加预约信息
        MeetingSchedule meetingSchedule = new MeetingSchedule();
        meetingSchedule.setMeetingId(meetingReservationQuery.getId());
        meetingSchedule.setUserId(meetingReservationQuery.getUserId());
        meetingSchedule.setBeginTime(meetingReservationQuery.getBeginTime());
        meetingSchedule.setEndTime(meetingReservationQuery.getEndTime());
        meetingSchedule.setTitle(meetingReservationQuery.getTitle());
        meetingSchedule.setCreateTime(DateUtils.getNowDate());
        meetingSchedule.setPhone(meetingReservationQuery.getPhone());
        meetingScheduleMapper.insertMeetingSchedule(meetingSchedule);
        return true;
    }


}
