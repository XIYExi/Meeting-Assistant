package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingGuest;

/**
 * 会议嘉宾Service接口
 * 
 * @author xiye
 * @date 2024-12-25
 */
public interface IMeetingGuestService 
{
    /**
     * 查询会议嘉宾
     * 
     * @param id 会议嘉宾主键
     * @return 会议嘉宾
     */
    public MeetingGuest selectMeetingGuestById(Long id);

    /**
     * 查询会议嘉宾列表
     * 
     * @param meetingGuest 会议嘉宾
     * @return 会议嘉宾集合
     */
    public List<MeetingGuest> selectMeetingGuestList(MeetingGuest meetingGuest);

    /**
     * 新增会议嘉宾
     * 
     * @param meetingGuest 会议嘉宾
     * @return 结果
     */
    public int insertMeetingGuest(MeetingGuest meetingGuest);

    /**
     * 修改会议嘉宾
     * 
     * @param meetingGuest 会议嘉宾
     * @return 结果
     */
    public int updateMeetingGuest(MeetingGuest meetingGuest);

    /**
     * 批量删除会议嘉宾
     * 
     * @param ids 需要删除的会议嘉宾主键集合
     * @return 结果
     */
    public int deleteMeetingGuestByIds(Long[] ids);

    /**
     * 删除会议嘉宾信息
     * 
     * @param id 会议嘉宾主键
     * @return 结果
     */
    public int deleteMeetingGuestById(Long id);
}
