package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingSchedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议预约Mapper接口
 * 
 * @author xiye
 * @date 2025-02-03
 */
@Mapper
public interface MeetingScheduleMapper 
{
    /**
     * 查询会议预约
     * 
     * @param id 会议预约主键
     * @return 会议预约
     */
    public MeetingSchedule selectMeetingScheduleById(Long id);

    /**
     * 查询会议预约列表
     * 
     * @param meetingSchedule 会议预约
     * @return 会议预约集合
     */
    public List<MeetingSchedule> selectMeetingScheduleList(MeetingSchedule meetingSchedule);

    /**
     * 新增会议预约
     * 
     * @param meetingSchedule 会议预约
     * @return 结果
     */
    public int insertMeetingSchedule(MeetingSchedule meetingSchedule);

    /**
     * 修改会议预约
     * 
     * @param meetingSchedule 会议预约
     * @return 结果
     */
    public int updateMeetingSchedule(MeetingSchedule meetingSchedule);

    /**
     * 删除会议预约
     * 
     * @param id 会议预约主键
     * @return 结果
     */
    public int deleteMeetingScheduleById(Long id);

    /**
     * 批量删除会议预约
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMeetingScheduleByIds(Long[] ids);
}
