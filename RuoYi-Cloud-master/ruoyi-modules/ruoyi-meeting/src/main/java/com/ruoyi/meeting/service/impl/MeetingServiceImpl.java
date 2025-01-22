package com.ruoyi.meeting.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.api.RemoteCosService;
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
        return meetingMapper.insertMeeting(meeting);
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
