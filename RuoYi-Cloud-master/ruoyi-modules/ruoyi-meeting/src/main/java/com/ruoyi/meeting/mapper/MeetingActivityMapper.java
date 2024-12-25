package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingActivity;

/**
 * 会议活动Mapper接口
 * 
 * @author xiye
 * @date 2024-12-25
 */
public interface MeetingActivityMapper 
{
    /**
     * 查询会议活动
     * 
     * @param id 会议活动主键
     * @return 会议活动
     */
    public MeetingActivity selectMeetingActivityById(Long id);

    /**
     * 查询会议活动列表
     * 
     * @param meetingActivity 会议活动
     * @return 会议活动集合
     */
    public List<MeetingActivity> selectMeetingActivityList(MeetingActivity meetingActivity);

    /**
     * 新增会议活动
     * 
     * @param meetingActivity 会议活动
     * @return 结果
     */
    public int insertMeetingActivity(MeetingActivity meetingActivity);

    /**
     * 修改会议活动
     * 
     * @param meetingActivity 会议活动
     * @return 结果
     */
    public int updateMeetingActivity(MeetingActivity meetingActivity);

    /**
     * 删除会议活动
     * 
     * @param id 会议活动主键
     * @return 结果
     */
    public int deleteMeetingActivityById(Long id);

    /**
     * 批量删除会议活动
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMeetingActivityByIds(Long[] ids);
}
