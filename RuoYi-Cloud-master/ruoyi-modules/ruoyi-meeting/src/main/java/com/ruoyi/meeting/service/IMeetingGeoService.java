package com.ruoyi.meeting.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ruoyi.meeting.domain.MeetingGeo;

/**
 * 会议地图Service接口
 * 
 * @author xiye
 * @date 2025-02-23
 */
public interface IMeetingGeoService 
{

    /**
     * 路径规划，计算驾车路径
     * @return
     */
    public List calPathPlanning(String origins, String distributions);


    /**
     * 查询会议地图
     * 
     * @param id 会议地图主键
     * @return 会议地图
     */
    public MeetingGeo selectMeetingGeoById(Long id);

    /**
     * 查询会议地图列表
     * 
     * @param meetingGeo 会议地图
     * @return 会议地图集合
     */
    public List<MeetingGeo> selectMeetingGeoList(MeetingGeo meetingGeo);

    /**
     * 新增会议地图
     * 
     * @param meetingGeo 会议地图
     * @return 结果
     */
    public int insertMeetingGeo(MeetingGeo meetingGeo);

    /**
     * 修改会议地图
     * 
     * @param meetingGeo 会议地图
     * @return 结果
     */
    public int updateMeetingGeo(MeetingGeo meetingGeo);

    /**
     * 批量删除会议地图
     * 
     * @param ids 需要删除的会议地图主键集合
     * @return 结果
     */
    public int deleteMeetingGeoByIds(Long[] ids);

    /**
     * 删除会议地图信息
     * 
     * @param id 会议地图主键
     * @return 结果
     */
    public int deleteMeetingGeoById(Long id);
}
