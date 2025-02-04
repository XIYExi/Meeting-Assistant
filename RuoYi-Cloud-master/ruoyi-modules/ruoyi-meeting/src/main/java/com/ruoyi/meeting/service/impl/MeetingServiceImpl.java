package com.ruoyi.meeting.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.qo.SysJobQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingMapper;
import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.service.IMeetingService;

/**
 * 会议Service业务层处理
 * 
 * @author xiye
 * @date 2024-12-25
 */
@Service
public class MeetingServiceImpl implements IMeetingService 
{
    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private RemoteCosService remoteCosService;

    /**
     * 查询会议
     * 
     * @param id 会议主键
     * @return 会议
     */
    @Override
    public Meeting selectMeetingById(Long id)
    {
        return meetingMapper.selectMeetingById(id);
    }

    /**
     * 查询会议列表
     * 
     * @param meeting 会议
     * @return 会议
     */
    @Override
    public List<Meeting> selectMeetingList(Meeting meeting)
    {
        return meetingMapper.selectMeetingList(meeting);
    }

    /**
     * 新增会议
     * 
     * @param meeting 会议
     * @return 结果
     */
    @Override
    public int insertMeeting(Meeting meeting)
    {
        meeting.setCreateTime(DateUtils.getNowDate());
        int i = meetingMapper.insertMeeting(meeting);
        if (1 == i) {
            // 添加 自动开始 定时任务
            SysJobQuery sysJobQuery = SysJobQuery.builder()
                    .jobName(meeting.getTitle() + "会议开始任务")
                    .jobGroup("DEFAULT")
                    .invokeTarget(String.format("ryTask.autoInjectMeetingBeginTask(%dL)", meeting.getId()))
                    // todo 需要把会议开始时间，翻译成cron表达式
                    .cronExpression(convertToCron(meeting.getBeginTime()))
                    .misfirePolicy("1")
                    .concurrent("1")
                    .status("0")
                    .build();
            sysJobQuery.setCreateTime(DateUtils.getNowDate());
            // todo 发送feign请求调用 job 模块执行插入会议自动开始


            // 再添加 自动结束 定时任务
            sysJobQuery.setJobName(meeting.getTitle() + " 会议结束任务");
            sysJobQuery.setInvokeTarget(String.format("ryTask.autoInjectMeetingEndTask(%dL)", meeting.getId()));
            sysJobQuery.setCronExpression(convertToCron(meeting.getEndTime()));
            sysJobQuery.setCreateTime(DateUtils.getNowDate());
            // todo 再次发送feign请求调用 job 插入会议自动结束


            return i;
        }
        else
            return 0;
    }

    public static String convertToCron(Date dateTime) {
        try {
            // 获取年、月、日、时、分、秒
            int year = dateTime.getYear() + 1900; // Date.getYear()返回的是自1900年以来的年数
            int month = dateTime.getMonth() + 1; // Date.getMonth()返回的是0-11
            int day = dateTime.getDate();
            int hour = dateTime.getHours();
            int minute = dateTime.getMinutes();
            int second = dateTime.getSeconds();

            // 构造Cron表达式
            // 注意：Cron表达式中的月份是从1开始的，而Date.getMonth()是从0开始的
            String cronExpression = String.format("%d %d %d %d %d", second, minute, hour, day, month);
            return cronExpression;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 修改会议
     * 
     * @param meeting 会议
     * @return 结果
     */
    @Override
    public int updateMeeting(Meeting meeting)
    {
        meeting.setUpdateTime(DateUtils.getNowDate());
        return meetingMapper.updateMeeting(meeting);
    }

    /**
     * 批量删除会议
     * 
     * @param ids 需要删除的会议主键
     * @return 结果
     */
    @Override
    public int deleteMeetingByIds(Long[] ids)
    {
        Arrays.stream(ids).forEach(meetingId -> {
            Meeting meeting = meetingMapper.selectMeetingById(meetingId);
            String url = meeting.getUrl();
            if (!url.equals("null")) {
                remoteCosService.removeImage(url);
            }
        });
        return meetingMapper.deleteMeetingByIds(ids);
    }

    /**
     * 删除会议信息
     * 
     * @param id 会议主键
     * @return 结果
     */
    @Override
    public int deleteMeetingById(Long id)
    {
        Meeting meeting = meetingMapper.selectMeetingById(id);
        String url = meeting.getUrl();
        if (!url.equals("null")) {
            remoteCosService.removeImage(url);
        }
        return meetingMapper.deleteMeetingById(id);
    }
}
