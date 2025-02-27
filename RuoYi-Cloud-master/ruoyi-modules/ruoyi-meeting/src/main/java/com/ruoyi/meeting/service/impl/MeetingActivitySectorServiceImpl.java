package com.ruoyi.meeting.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.domain.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingActivitySectorMapper;
import com.ruoyi.meeting.domain.MeetingActivitySector;
import com.ruoyi.meeting.service.IMeetingActivitySectorService;

import javax.annotation.Resource;

/**
 * 会议活动板块Service业务层处理
 * 
 * @author xiye
 * @date 2024-12-25
 */
@Service
public class MeetingActivitySectorServiceImpl implements IMeetingActivitySectorService 
{
    @Autowired
    private MeetingActivitySectorMapper meetingActivitySectorMapper;
    @Resource
    private RemoteCosService remoteCosService;

    /**
     * 查询会议活动板块
     * 
     * @param id 会议活动板块主键
     * @return 会议活动板块
     */
    @Override
    public MeetingActivitySector selectMeetingActivitySectorById(Long id)
    {
        return meetingActivitySectorMapper.selectMeetingActivitySectorById(id);
    }

    /**
     * 查询会议活动板块列表
     * 
     * @param meetingActivitySector 会议活动板块
     * @return 会议活动板块
     */
    @Override
    public List<MeetingActivitySector> selectMeetingActivitySectorList(MeetingActivitySector meetingActivitySector)
    {
        return meetingActivitySectorMapper.selectMeetingActivitySectorList(meetingActivitySector);
    }

    /**
     * 新增会议活动板块
     * 
     * @param meetingActivitySector 会议活动板块
     * @return 结果
     */
    @Override
    public int insertMeetingActivitySector(MeetingActivitySector meetingActivitySector)
    {
        meetingActivitySector.setCreateTime(DateUtils.getNowDate());
        return meetingActivitySectorMapper.insertMeetingActivitySector(meetingActivitySector);
    }

    /**
     * 修改会议活动板块
     * 
     * @param meetingActivitySector 会议活动板块
     * @return 结果
     */
    @Override
    public int updateMeetingActivitySector(MeetingActivitySector meetingActivitySector)
    {
        meetingActivitySector.setUpdateTime(DateUtils.getNowDate());
        return meetingActivitySectorMapper.updateMeetingActivitySector(meetingActivitySector);
    }

    /**
     * 批量删除会议活动板块
     * 
     * @param ids 需要删除的会议活动板块主键
     * @return 结果
     */
    @Override
    public int deleteMeetingActivitySectorByIds(Long[] ids)
    {
        Arrays.stream(ids).forEach(newsId -> {
            MeetingActivitySector meetingActivitySector = meetingActivitySectorMapper.selectMeetingActivitySectorById(newsId);
            String url = meetingActivitySector.getUrl();
            if (!url.equals("null")) {
                remoteCosService.removeImage(url);
            }
        });
        return meetingActivitySectorMapper.deleteMeetingActivitySectorByIds(ids);
    }

    /**
     * 删除会议活动板块信息
     * 
     * @param id 会议活动板块主键
     * @return 结果
     */
    @Override
    public int deleteMeetingActivitySectorById(Long id)
    {
        MeetingActivitySector meetingActivitySector = meetingActivitySectorMapper.selectMeetingActivitySectorById(id);
        String url = meetingActivitySector.getUrl();
        if (!url.equals("null")) {
            remoteCosService.removeImage(url);
        }
        return meetingActivitySectorMapper.deleteMeetingActivitySectorById(id);
    }
}
