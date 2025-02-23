package com.ruoyi.meeting.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.MeetingGeoMapper;
import com.ruoyi.meeting.domain.MeetingGeo;
import com.ruoyi.meeting.service.IMeetingGeoService;

/**
 * 会议地图Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-23
 */
@Service
public class MeetingGeoServiceImpl implements IMeetingGeoService 
{
    @Autowired
    private MeetingGeoMapper meetingGeoMapper;

    /**
     * 查询会议地图
     * 
     * @param id 会议地图主键
     * @return 会议地图
     */
    @Override
    public MeetingGeo selectMeetingGeoById(Long id)
    {
        return meetingGeoMapper.selectMeetingGeoById(id);
    }

    
    /**
     * 查询会议地图列表
     * 
     * @param meetingGeo 会议地图
     * @return 会议地图
     */
    @Override
    public List<MeetingGeo> selectMeetingGeoList(MeetingGeo meetingGeo)
    {
        return meetingGeoMapper.selectMeetingGeoList(meetingGeo);
    }

    /**
     * 新增会议地图
     * 
     * @param meetingGeo 会议地图
     * @return 结果
     */
    @Override
    public int insertMeetingGeo(MeetingGeo meetingGeo)
    {
        meetingGeo.setCreateTime(DateUtils.getNowDate());
        return meetingGeoMapper.insertMeetingGeo(meetingGeo);
    }

    /**
     * 修改会议地图
     * 
     * @param meetingGeo 会议地图
     * @return 结果
     */
    @Override
    public int updateMeetingGeo(MeetingGeo meetingGeo)
    {
        meetingGeo.setUpdateTime(DateUtils.getNowDate());
        return meetingGeoMapper.updateMeetingGeo(meetingGeo);
    }

    /**
     * 批量删除会议地图
     * 
     * @param ids 需要删除的会议地图主键
     * @return 结果
     */
    @Override
    public int deleteMeetingGeoByIds(Long[] ids)
    {
        return meetingGeoMapper.deleteMeetingGeoByIds(ids);
    }

    /**
     * 删除会议地图信息
     * 
     * @param id 会议地图主键
     * @return 结果
     */
    @Override
    public int deleteMeetingGeoById(Long id)
    {
        return meetingGeoMapper.deleteMeetingGeoById(id);
    }
}
