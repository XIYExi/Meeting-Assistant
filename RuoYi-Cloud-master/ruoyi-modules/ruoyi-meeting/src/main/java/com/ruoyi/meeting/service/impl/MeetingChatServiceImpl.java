package com.ruoyi.meeting.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingChatMapper;
import com.ruoyi.meeting.domain.MeetingChat;
import com.ruoyi.meeting.service.IMeetingChatService;

/**
 * 会议聊天Service业务层处理
 * 
 * @author xiye
 * @date 2024-12-25
 */
@Service
public class MeetingChatServiceImpl implements IMeetingChatService 
{
    @Autowired
    private MeetingChatMapper meetingChatMapper;

    /**
     * 查询会议聊天
     * 
     * @param id 会议聊天主键
     * @return 会议聊天
     */
    @Override
    public MeetingChat selectMeetingChatById(Long id)
    {
        return meetingChatMapper.selectMeetingChatById(id);
    }

    /**
     * 查询会议聊天列表
     * 
     * @param meetingChat 会议聊天
     * @return 会议聊天
     */
    @Override
    public List<MeetingChat> selectMeetingChatList(MeetingChat meetingChat)
    {
        return meetingChatMapper.selectMeetingChatList(meetingChat);
    }

    /**
     * 新增会议聊天
     * 
     * @param meetingChat 会议聊天
     * @return 结果
     */
    @Override
    public int insertMeetingChat(MeetingChat meetingChat)
    {
        meetingChat.setCreateTime(DateUtils.getNowDate());
        return meetingChatMapper.insertMeetingChat(meetingChat);
    }

    /**
     * 修改会议聊天
     * 
     * @param meetingChat 会议聊天
     * @return 结果
     */
    @Override
    public int updateMeetingChat(MeetingChat meetingChat)
    {
        meetingChat.setUpdateTime(DateUtils.getNowDate());
        return meetingChatMapper.updateMeetingChat(meetingChat);
    }

    /**
     * 批量删除会议聊天
     * 
     * @param ids 需要删除的会议聊天主键
     * @return 结果
     */
    @Override
    public int deleteMeetingChatByIds(Long[] ids)
    {
        return meetingChatMapper.deleteMeetingChatByIds(ids);
    }

    /**
     * 删除会议聊天信息
     * 
     * @param id 会议聊天主键
     * @return 结果
     */
    @Override
    public int deleteMeetingChatById(Long id)
    {
        return meetingChatMapper.deleteMeetingChatById(id);
    }
}
