package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingClip;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议附件Mapper接口
 * 
 * @author xiye
 * @date 2025-02-22
 */
@Mapper
public interface MeetingClipMapper 
{
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
     * 删除会议附件
     * 
     * @param id 会议附件主键
     * @return 结果
     */
    public int deleteMeetingClipById(Long id);

    /**
     * 批量删除会议附件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMeetingClipByIds(Long[] ids);
}
