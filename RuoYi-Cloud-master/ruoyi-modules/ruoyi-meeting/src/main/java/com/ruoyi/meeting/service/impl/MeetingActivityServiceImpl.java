package com.ruoyi.meeting.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.domain.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingActivityMapper;
import com.ruoyi.meeting.domain.MeetingActivity;
import com.ruoyi.meeting.service.IMeetingActivityService;

import javax.annotation.Resource;

/**
 * 会议活动Service业务层处理
 * 
 * @author xiye
 * @date 2024-12-25
 */
@Service
public class MeetingActivityServiceImpl implements IMeetingActivityService 
{
    @Autowired
    private MeetingActivityMapper meetingActivityMapper;
    @Resource
    private RemoteCosService remoteCosService;

    /**
     * 查询会议活动
     * 
     * @param id 会议活动主键
     * @return 会议活动
     */
    @Override
    public MeetingActivity selectMeetingActivityById(Long id)
    {
        return meetingActivityMapper.selectMeetingActivityById(id);
    }

    /**
     * 查询会议活动列表
     * 
     * @param meetingActivity 会议活动
     * @return 会议活动
     */
    @Override
    public List<MeetingActivity> selectMeetingActivityList(MeetingActivity meetingActivity)
    {
        return meetingActivityMapper.selectMeetingActivityList(meetingActivity);
    }

    /**
     * 新增会议活动
     * 
     * @param meetingActivity 会议活动
     * @return 结果
     */
    @Override
    public int insertMeetingActivity(MeetingActivity meetingActivity)
    {
        meetingActivity.setCreateTime(DateUtils.getNowDate());
        return meetingActivityMapper.insertMeetingActivity(meetingActivity);
    }

    /**
     * 修改会议活动
     * 
     * @param meetingActivity 会议活动
     * @return 结果
     */
    @Override
    public int updateMeetingActivity(MeetingActivity meetingActivity)
    {
        meetingActivity.setUpdateTime(DateUtils.getNowDate());
        return meetingActivityMapper.updateMeetingActivity(meetingActivity);
    }

    /**
     * 批量删除会议活动
     * 
     * @param ids 需要删除的会议活动主键
     * @return 结果
     */
    @Override
    public int deleteMeetingActivityByIds(Long[] ids)
    {
        Arrays.stream(ids).forEach(newsId -> {
            MeetingActivity meetingActivity = meetingActivityMapper.selectMeetingActivityById(newsId);
            String url = meetingActivity.getUrl();
            if (!url.equals("null")) {
                remoteCosService.removeImage(url);
            }
        });
        return meetingActivityMapper.deleteMeetingActivityByIds(ids);
    }

    /**
     * 删除会议活动信息
     * 
     * @param id 会议活动主键
     * @return 结果
     */
    @Override
    public int deleteMeetingActivityById(Long id)
    {
        MeetingActivity meetingActivity = meetingActivityMapper.selectMeetingActivityById(id);
        String url = meetingActivity.getUrl();
        if (!url.equals("null")) {
            remoteCosService.removeImage(url);
        }
        return meetingActivityMapper.deleteMeetingActivityById(id);
    }
}
