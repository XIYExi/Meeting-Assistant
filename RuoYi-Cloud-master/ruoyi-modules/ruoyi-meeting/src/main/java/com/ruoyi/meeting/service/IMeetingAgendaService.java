package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingAgenda;
import org.apache.ibatis.annotations.Param;

/**
 * 会议议程Service接口
 * 
 * @author xiye
 * @date 2024-12-25
 */
public interface IMeetingAgendaService 
{
    /**
     * 查询会议议程
     * 
     * @param id 会议议程主键
     * @return 会议议程
     */
    public MeetingAgenda selectMeetingAgendaById(Long id);


    public List<MeetingAgenda> selectMeetingAgendaByMeetingId(Long meetingId);
    /**
     * 查询会议议程列表
     * 
     * @param meetingAgenda 会议议程
     * @return 会议议程集合
     */
    public List<MeetingAgenda> selectMeetingAgendaList(MeetingAgenda meetingAgenda);

    /**
     * 新增会议议程
     * 
     * @param meetingAgenda 会议议程
     * @return 结果
     */
    public int insertMeetingAgenda(MeetingAgenda meetingAgenda);

    /**
     * 修改会议议程
     * 
     * @param meetingAgenda 会议议程
     * @return 结果
     */
    public int updateMeetingAgenda(MeetingAgenda meetingAgenda);

    /**
     * 批量删除会议议程
     * 
     * @param ids 需要删除的会议议程主键集合
     * @return 结果
     */
    public int deleteMeetingAgendaByIds(Long[] ids);

    /**
     * 删除会议议程信息
     * 
     * @param id 会议议程主键
     * @return 结果
     */
    public int deleteMeetingAgendaById(Long id);
}
