package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.MeetingGeo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议地图Mapper接口
 * 
 * @author xiye
 * @date 2025-02-23
 */
@Mapper
public interface MeetingGeoMapper 
{
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
     * 删除会议地图
     * 
     * @param id 会议地图主键
     * @return 结果
     */
    public int deleteMeetingGeoById(Long id);

    /**
     * 批量删除会议地图
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMeetingGeoByIds(Long[] ids);
}
