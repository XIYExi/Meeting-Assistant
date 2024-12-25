package com.ruoyi.meeting.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingGuestMapper;
import com.ruoyi.meeting.domain.MeetingGuest;
import com.ruoyi.meeting.service.IMeetingGuestService;

/**
 * 会议嘉宾Service业务层处理
 * 
 * @author xiye
 * @date 2024-12-25
 */
@Service
public class MeetingGuestServiceImpl implements IMeetingGuestService 
{
    @Autowired
    private MeetingGuestMapper meetingGuestMapper;

    /**
     * 查询会议嘉宾
     * 
     * @param id 会议嘉宾主键
     * @return 会议嘉宾
     */
    @Override
    public MeetingGuest selectMeetingGuestById(Long id)
    {
        return meetingGuestMapper.selectMeetingGuestById(id);
    }

    /**
     * 查询会议嘉宾列表
     * 
     * @param meetingGuest 会议嘉宾
     * @return 会议嘉宾
     */
    @Override
    public List<MeetingGuest> selectMeetingGuestList(MeetingGuest meetingGuest)
    {
        return meetingGuestMapper.selectMeetingGuestList(meetingGuest);
    }

    /**
     * 新增会议嘉宾
     * 
     * @param meetingGuest 会议嘉宾
     * @return 结果
     */
    @Override
    public int insertMeetingGuest(MeetingGuest meetingGuest)
    {
        meetingGuest.setCreateTime(DateUtils.getNowDate());
        return meetingGuestMapper.insertMeetingGuest(meetingGuest);
    }

    /**
     * 修改会议嘉宾
     * 
     * @param meetingGuest 会议嘉宾
     * @return 结果
     */
    @Override
    public int updateMeetingGuest(MeetingGuest meetingGuest)
    {
        meetingGuest.setUpdateTime(DateUtils.getNowDate());
        return meetingGuestMapper.updateMeetingGuest(meetingGuest);
    }

    /**
     * 批量删除会议嘉宾
     * 
     * @param ids 需要删除的会议嘉宾主键
     * @return 结果
     */
    @Override
    public int deleteMeetingGuestByIds(Long[] ids)
    {
        return meetingGuestMapper.deleteMeetingGuestByIds(ids);
    }

    /**
     * 删除会议嘉宾信息
     * 
     * @param id 会议嘉宾主键
     * @return 结果
     */
    @Override
    public int deleteMeetingGuestById(Long id)
    {
        return meetingGuestMapper.deleteMeetingGuestById(id);
    }
}
