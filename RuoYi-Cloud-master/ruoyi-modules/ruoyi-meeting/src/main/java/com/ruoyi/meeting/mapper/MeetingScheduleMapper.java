package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingSchedule;
import com.ruoyi.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     *  会议详细页面中使用
     *  查出当前会议参与人数
     * @param id
     * @return
     */
    public Integer selectPartsNumberById(@Param("id") Long id);

    /**
     * 查询当前会议报名数据
     * @param meetingId
     * @return
     */
    public List<SysUser> selectPartUserList(@Param("meetingId")Long meetingId);


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
