package com.ruoyi.meeting.service.impl;

import java.util.List;
import java.util.Objects;

import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.cos.api.RemoteCosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingClipMapper;
import com.ruoyi.meeting.domain.MeetingClip;
import com.ruoyi.meeting.service.IMeetingClipService;

import javax.annotation.Resource;

/**
 * 会议附件Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-22
 */
@Service
public class MeetingClipServiceImpl implements IMeetingClipService 
{
    @Autowired
    private MeetingClipMapper meetingClipMapper;
    @Resource
    private RemoteCosService remoteCosService;

    @Override
    public List<MeetingClip> selectMeetingClipsByCond(long id, long type) {
        MeetingClip meetingClip = new MeetingClip();
        meetingClip.setClipType(type);
        if (Objects.equals(type, 1L)) {
            // 查meetingId
            meetingClip.setMeetingId(id);
        }
        else if (Objects.equals(type, 2L)) {
            // 查agendaId
            meetingClip.setAgendaId(id);
        }
        else {
            // 查其他 meetingId和agendaId都为0
            meetingClip.setMeetingId(0L);
            meetingClip.setAgendaId(0L);
        }
        List<MeetingClip> meetingClips = meetingClipMapper.selectMeetingClipList(meetingClip);
        return meetingClips;
    }

    /**
     * 查询会议附件
     * 
     * @param id 会议附件主键
     * @return 会议附件
     */
    @Override
    public MeetingClip selectMeetingClipById(Long id)
    {
        return meetingClipMapper.selectMeetingClipById(id);
    }

    /**
     * 查询会议附件列表
     * 
     * @param meetingClip 会议附件
     * @return 会议附件
     */
    @Override
    public List<MeetingClip> selectMeetingClipList(MeetingClip meetingClip)
    {
        return meetingClipMapper.selectMeetingClipList(meetingClip);
    }

    /**
     * 新增会议附件
     * 
     * @param meetingClip 会议附件
     * @return 结果
     */
    @Override
    public int insertMeetingClip(MeetingClip meetingClip)
    {
        meetingClip.setCreateTime(DateUtils.getNowDate());
        return meetingClipMapper.insertMeetingClip(meetingClip);
    }

    /**
     * 修改会议附件
     * 
     * @param meetingClip 会议附件
     * @return 结果
     */
    @Override
    public int updateMeetingClip(MeetingClip meetingClip)
    {
        meetingClip.setUpdateTime(DateUtils.getNowDate());
        return meetingClipMapper.updateMeetingClip(meetingClip);
    }

    /**
     * 批量删除会议附件
     * 
     * @param ids 需要删除的会议附件主键
     * @return 结果
     */
    @Override
    public int deleteMeetingClipByIds(Long[] ids)
    {
        return meetingClipMapper.deleteMeetingClipByIds(ids);
    }

    /**
     * 删除会议附件信息
     * 
     * @param id 会议附件主键
     * @return 结果
     */
    @Override
    public int deleteMeetingClipById(Long id)
    {
        return meetingClipMapper.deleteMeetingClipById(id);
    }

    @Override
    public boolean delCip(Long id) {
        MeetingClip meetingClip = meetingClipMapper.selectMeetingClipById(id);
        remoteCosService.removeClip(meetingClip.getUrl());
        meetingClipMapper.deleteMeetingClipById(id);
        return true;
    }
}
