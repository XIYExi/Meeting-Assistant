package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingActivitySector;

/**
 * 会议活动板块Service接口
 * 
 * @author xiye
 * @date 2024-12-25
 */
public interface IMeetingActivitySectorService 
{
    /**
     * 查询会议活动板块
     * 
     * @param id 会议活动板块主键
     * @return 会议活动板块
     */
    public MeetingActivitySector selectMeetingActivitySectorById(Long id);

    /**
     * 查询会议活动板块列表
     * 
     * @param meetingActivitySector 会议活动板块
     * @return 会议活动板块集合
     */
    public List<MeetingActivitySector> selectMeetingActivitySectorList(MeetingActivitySector meetingActivitySector);

    /**
     * 新增会议活动板块
     * 
     * @param meetingActivitySector 会议活动板块
     * @return 结果
     */
    public int insertMeetingActivitySector(MeetingActivitySector meetingActivitySector);

    /**
     * 修改会议活动板块
     * 
     * @param meetingActivitySector 会议活动板块
     * @return 结果
     */
    public int updateMeetingActivitySector(MeetingActivitySector meetingActivitySector);

    /**
     * 批量删除会议活动板块
     * 
     * @param ids 需要删除的会议活动板块主键集合
     * @return 结果
     */
    public int deleteMeetingActivitySectorByIds(Long[] ids);

    /**
     * 删除会议活动板块信息
     * 
     * @param id 会议活动板块主键
     * @return 结果
     */
    public int deleteMeetingActivitySectorById(Long id);
}
