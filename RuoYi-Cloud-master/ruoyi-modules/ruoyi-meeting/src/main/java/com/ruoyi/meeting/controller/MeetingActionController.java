package com.ruoyi.meeting.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.qo.MeetingReservationQuery;
import com.ruoyi.meeting.service.IRecordScheduleService;
import com.ruoyi.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetingAction")
public class MeetingActionController {


    @Autowired
    private IRecordScheduleService recordScheduleService;


    /**
     * 【预约会议】
     * 用户在app中点击预约会议，提交预约信息到数据库，每晚8点定时扫库发送提醒信息
     * @return
     */
    @PostMapping("/reservation")
    public AjaxResult reservation(@RequestBody MeetingReservationQuery meetingReservationQuery){
        boolean b = recordScheduleService.MeetingReservation(meetingReservationQuery);
        return b ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 【参会者列表】
     * @param meetingId
     * @return
     */
    @PostMapping("/list")
    public AjaxResult list(@RequestParam("meetingId") Long meetingId) {
        List<SysUser> sysUsers = recordScheduleService.MeetingPartList(meetingId);
        return AjaxResult.success(sysUsers);
    }


    /**
     * 【取消会议预约】
     * @param id
     * @return
     */
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestParam("id") Long id) {
        boolean b = recordScheduleService.MeetingPartRemove(id);
        return b ? AjaxResult.success() : AjaxResult.error();
    }
}
