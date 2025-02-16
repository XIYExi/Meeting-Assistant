package com.ruoyi.meeting.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.job.api.RemoteSysJobService;
import com.ruoyi.job.api.domain.SysJob;
import com.ruoyi.meeting.entity.SimplePartUser;
import com.ruoyi.meeting.mapper.MeetingScheduleMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class MeetingServiceImpl implements IMeetingService 
{
    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private RemoteCosService remoteCosService;
    @Autowired
    private RemoteSysJobService remoteSysJobService;
    @Autowired
    private MeetingScheduleMapper meetingScheduleMapper;

    @Override
    public SimplePartUser getPartUserAvatarById(Long id) {
        SimplePartUser simplePartUser = new SimplePartUser();
        List<String> partUserAvatarById = meetingMapper.getPartUserAvatarById(id);
        Integer total = meetingScheduleMapper.selectPartsNumberById(id);
        simplePartUser.setParts(total);
        simplePartUser.setAvatars(partUserAvatarById);
        return simplePartUser;
    }

    @Override
    public List<String> selectMeetingBeginTimeForList() {
        return meetingMapper.selectMeetingBeginTimeForList();
    }

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
            SysJob sysJobQuery = new SysJob();
            sysJobQuery.setJobName(meeting.getTitle() + "会议开始任务");
            sysJobQuery.setJobGroup("DEFAULT");
            sysJobQuery.setInvokeTarget(String.format("ryTask.autoInjectMeetingBeginTask(%dL)", meeting.getId()));
            // 需要把会议开始时间，翻译成cron表达式
            sysJobQuery.setCronExpression(convertToCron(meeting.getBeginTime()));
            sysJobQuery.setMisfirePolicy("1");
            sysJobQuery.setConcurrent("1");
            sysJobQuery.setStatus("0");
            sysJobQuery.setCreateTime(DateUtils.getNowDate());
            // 发送feign请求调用 job 模块执行插入会议自动开始
            try {
                AjaxResult addBeginResult = remoteSysJobService.add(sysJobQuery);
                //System.err.println(addBeginResult);
                log.info(addBeginResult.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }


            // 再添加 自动结束 定时任务
            sysJobQuery.setJobName(meeting.getTitle() + " 会议结束任务");
            sysJobQuery.setInvokeTarget(String.format("ryTask.autoInjectMeetingEndTask(%dL)", meeting.getId()));
            sysJobQuery.setCronExpression(convertToCron(meeting.getEndTime()));
            sysJobQuery.setCreateTime(DateUtils.getNowDate());
            // todo 再次发送feign请求调用 job 插入会议自动结束
            try {
                AjaxResult addEndResult = remoteSysJobService.add(sysJobQuery);
                //System.err.println(addEndResult);
                log.info(addEndResult.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

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
            String cronExpression = String.format("%d %d %d %d %d ? %d", second, minute, hour, day, month, year);
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
            try {
                AjaxResult ajaxRemoveSysJobResult = remoteSysJobService.removeByMeetingId(meetingId);
                log.info("删除成功 {}", ajaxRemoveSysJobResult.toString());
            } catch (Exception e) {
                e.printStackTrace();
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
        try {
            AjaxResult ajaxRemoveSysJobResult = remoteSysJobService.removeByMeetingId(id);
            log.info("删除成功 {}", ajaxRemoveSysJobResult.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return meetingMapper.deleteMeetingById(id);
    }


}
