package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingClip;

/**
 * 会议附件Service接口
 * 
 * @author xiye
 * @date 2025-02-22
 */
public interface IMeetingClipService 
{

    public List<MeetingClip> selectMeetingClipsByCond(long id, long type);


    /**
     * 查询会议附件
     * 
     * @param id 会议附件主键
     * @return 会议附件
     */
    public MeetingClip selectMeetingClipById(Long id);

    /**
     * 查询会议附件列表
     * 
     * @param meetingClip 会议附件
     * @return 会议附件集合
     */
    public List<MeetingClip> selectMeetingClipList(MeetingClip meetingClip);

    /**
     * 新增会议附件
     * 
     * @param meetingClip 会议附件
     * @return 结果
     */
    public int insertMeetingClip(MeetingClip meetingClip);

    /**
     * 修改会议附件
     * 
     * @param meetingClip 会议附件
     * @return 结果
     */
    public int updateMeetingClip(MeetingClip meetingClip);

    /**
     * 批量删除会议附件
     * 
     * @param ids 需要删除的会议附件主键集合
     * @return 结果
     */
    public int deleteMeetingClipByIds(Long[] ids);

    /**
     * 删除会议附件信息
     * 
     * @param id 会议附件主键
     * @return 结果
     */
    public int deleteMeetingClipById(Long id);

    boolean delCip(Long id);
}
