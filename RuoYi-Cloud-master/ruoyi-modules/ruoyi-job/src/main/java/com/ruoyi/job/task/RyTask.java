package com.ruoyi.job.task;


import com.ruoyi.job.domain.SysJob;
import com.ruoyi.job.mapper.SysJobMapper;
import com.ruoyi.job.service.SysJobServiceImpl;
import com.ruoyi.meeting.api.RemoteScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.utils.StringUtils;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Slf4j
@Component("ryTask")
public class RyTask
{
    @Autowired
    private RemoteScheduleService remoteScheduleService;
    @Autowired
    private SysJobServiceImpl sysJobService;



    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }


    /**
     * 自动执行任务
     * 到时间自动将会议的状态修改为 进行中 status=2
     * 只会执行一次
     */
    public void autoInjectMeetingBeginTask(Long meetingId) {
        // openfeign调用meeting修改状态
        remoteScheduleService.updateMeetingStatus(meetingId, 2);
        String msg = String.format("会议编号 %d ,自动开始", meetingId);
        log.info(msg);

        // 关闭begin任务
        try {
            SysJob sysJob = sysJobService.selectBeginJobByInvoketarget(meetingId);
            sysJob.setStatus("1");
            sysJobService.changeStatus(sysJob);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 自动执行任务
     * 到时间自动将会议的状态修改为 已结束 status=3
     * 只会执行一次
     */
    public void autoInjectMeetingEndTask(Long meetingId) {
        // openfeign调用meeting修改状态
        remoteScheduleService.updateMeetingStatus(meetingId, 3);
        String msg = String.format("会议编号 %d ,自动结束", meetingId);
        log.info(msg);

        // 关闭end任务
        try {
            SysJob sysJob = sysJobService.selectEndJobByInvoketarget(meetingId);
            sysJob.setStatus("1");
            sysJobService.changeStatus(sysJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
