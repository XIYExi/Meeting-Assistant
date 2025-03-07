package com.ruoyi.rec.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.rec.domain.MeetingRate;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingRateMapper extends BaseMapper<MeetingRate> {

    /**
     * 清空会议评分表
     */
    @Delete("delete from meeting_rate")
    public void clearMeetingRate();

}
