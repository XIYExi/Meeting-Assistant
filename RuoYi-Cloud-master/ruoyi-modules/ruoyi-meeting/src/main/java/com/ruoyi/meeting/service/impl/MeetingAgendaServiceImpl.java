package com.ruoyi.meeting.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.rag.api.RemoteRagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingAgendaMapper;
import com.ruoyi.meeting.domain.MeetingAgenda;
import com.ruoyi.meeting.service.IMeetingAgendaService;

import javax.annotation.Resource;

/**
 * 会议议程Service业务层处理
 * 
 * @author xiye
 * @date 2024-12-25
 */
@Service
public class MeetingAgendaServiceImpl implements IMeetingAgendaService 
{
    @Autowired
    private MeetingAgendaMapper meetingAgendaMapper;
    @Resource
    private RemoteRagService remoteRagService;

    /**
     * 查询会议议程
     * 
     * @param id 会议议程主键
     * @return 会议议程
     */
    @Override
    public MeetingAgenda selectMeetingAgendaById(Long id)
    {
        return meetingAgendaMapper.selectMeetingAgendaById(id);
    }

    @Override
    public List<MeetingAgenda> selectMeetingAgendaByMeetingId(Long meetingId) {
        return meetingAgendaMapper.selectMeetingAgendaByMeetingId(meetingId);
    }

    /**
     * 查询会议议程列表
     * 
     * @param meetingAgenda 会议议程
     * @return 会议议程
     */
    @Override
    public List<MeetingAgenda> selectMeetingAgendaList(MeetingAgenda meetingAgenda)
    {
        return meetingAgendaMapper.selectMeetingAgendaList(meetingAgenda);
    }

    /**
     * 新增会议议程
     * 
     * @param meetingAgenda 会议议程
     * @return 结果
     */
    @Override
    public int insertMeetingAgenda(MeetingAgenda meetingAgenda)
    {
        meetingAgenda.setCreateTime(DateUtils.getNowDate());
        int i = meetingAgendaMapper.insertMeetingAgenda(meetingAgenda);
        // 2025.03.04 会议创建成功之后插入 Milvus
        remoteRagService.insert(meetingAgenda.getId(), 2L, meetingAgenda.getContent());
        return i;
    }

    /**
     * 修改会议议程
     * 
     * @param meetingAgenda 会议议程
     * @return 结果
     */
    @Override
    public int updateMeetingAgenda(MeetingAgenda meetingAgenda)
    {
        meetingAgenda.setUpdateTime(DateUtils.getNowDate());
        return meetingAgendaMapper.updateMeetingAgenda(meetingAgenda);
    }

    /**
     * 批量删除会议议程
     * 
     * @param ids 需要删除的会议议程主键
     * @return 结果
     */
    @Override
    public int deleteMeetingAgendaByIds(Long[] ids)
    {
        return meetingAgendaMapper.deleteMeetingAgendaByIds(ids);
    }

    /**
     * 删除会议议程信息
     * 
     * @param id 会议议程主键
     * @return 结果
     */
    @Override
    public int deleteMeetingAgendaById(Long id)
    {
        return meetingAgendaMapper.deleteMeetingAgendaById(id);
    }
}
