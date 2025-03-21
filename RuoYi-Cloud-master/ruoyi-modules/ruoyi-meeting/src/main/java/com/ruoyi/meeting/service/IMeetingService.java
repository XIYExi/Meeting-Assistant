package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.domain.MeetingGeo;
import com.ruoyi.meeting.entity.MeetingMilvusEntity;
import com.ruoyi.meeting.entity.SimplePartUser;

/**
 * 会议Service接口
 *
 * @author xiye
 * @date 2024-12-25
 */
public interface IMeetingService 
{

    public Meeting getLastOneMeeting();

    public MeetingGeo transferMeetingGeo(String location);

    public SimplePartUser getPartUserAvatarById(Long id);

    public List<String> selectMeetingBeginTimeForList();
    /**
     * 查询会议
     * 
     * @param id 会议主键
     * @return 会议
     */
    public Meeting selectMeetingById(Long id);

    /**
     * 查询会议列表
     * 
     * @param meeting 会议
     * @return 会议集合
     */
    public List<Meeting> selectMeetingList(Meeting meeting);

    /**
     * 新增会议
     * 
     * @param meeting 会议
     * @return 结果
     */
    public int insertMeeting(Meeting meeting);

    /**
     * 修改会议
     * 
     * @param meeting 会议
     * @return 结果
     */
    public int updateMeeting(Meeting meeting);

    /**
     * 批量删除会议
     * 
     * @param ids 需要删除的会议主键集合
     * @return 结果
     */
    public int deleteMeetingByIds(Long[] ids);

    /**
     * 删除会议信息
     * 
     * @param id 会议主键
     * @return 结果
     */
    public int deleteMeetingById(Long id);

    void insertMilvus(List<MeetingMilvusEntity> list);

    List<Meeting> selectMeetingListByStaticRec();
}
