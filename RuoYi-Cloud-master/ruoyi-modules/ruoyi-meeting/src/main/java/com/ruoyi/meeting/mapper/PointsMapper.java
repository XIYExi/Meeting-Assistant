package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.Points;
import com.ruoyi.meeting.domain.PointsRecord;
import com.ruoyi.meeting.entity.PointsRecordResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分Mapper接口
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Mapper
public interface PointsMapper 
{
    /**
     * 查询积分
     * 
     * @param id 积分主键
     * @return 积分
     */
    public Points selectPointsById(Long id);

    /**
     * 查询积分列表
     * 
     * @param points 积分
     * @return 积分集合
     */
    public List<Points> selectPointsList(Points points);

    /**
     * 新增积分
     * 
     * @param points 积分
     * @return 结果
     */
    public int insertPoints(Points points);

    /**
     * 修改积分
     * 
     * @param points 积分
     * @return 结果
     */
    public int updatePoints(Points points);

    /**
     * 删除积分
     * 
     * @param id 积分主键
     * @return 结果
     */
    public int deletePointsById(Long id);

    /**
     * 批量删除积分
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsByIds(Long[] ids);

    List<PointsRecordResponse> selectHistoryPointsRecord(@Param("userId") Long userId);


}
