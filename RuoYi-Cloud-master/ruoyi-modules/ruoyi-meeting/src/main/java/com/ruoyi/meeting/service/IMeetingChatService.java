package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingChat;

/**
 * 会议聊天Service接口
 * 
 * @author xiye
 * @date 2024-12-25
 */
public interface IMeetingChatService 
{
    /**
     * 查询会议聊天
     * 
     * @param id 会议聊天主键
     * @return 会议聊天
     */
    public MeetingChat selectMeetingChatById(Long id);

    /**
     * 查询会议聊天列表
     * 
     * @param meetingChat 会议聊天
     * @return 会议聊天集合
     */
    public List<MeetingChat> selectMeetingChatList(MeetingChat meetingChat);

    /**
     * 新增会议聊天
     * 
     * @param meetingChat 会议聊天
     * @return 结果
     */
    public int insertMeetingChat(MeetingChat meetingChat);

    /**
     * 修改会议聊天
     * 
     * @param meetingChat 会议聊天
     * @return 结果
     */
    public int updateMeetingChat(MeetingChat meetingChat);

    /**
     * 批量删除会议聊天
     * 
     * @param ids 需要删除的会议聊天主键集合
     * @return 结果
     */
    public int deleteMeetingChatByIds(Long[] ids);

    /**
     * 删除会议聊天信息
     * 
     * @param id 会议聊天主键
     * @return 结果
     */
    public int deleteMeetingChatById(Long id);
}
