package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.PointsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分记录Mapper接口
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Mapper
public interface PointsRecordMapper 
{

    List<PointsRecord> selectDailySignIn(@Param("pointId") Long pointId);

    public List<PointsRecord> selectUserDailyCompleteList(@Param("userId") Long userId, @Param("pointId") Long pointId);
    /**
     * 查询积分记录
     * 
     * @param id 积分记录主键
     * @return 积分记录
     */
    public PointsRecord selectPointsRecordById(Long id);

    /**
     * 查询积分记录列表
     * 
     * @param pointsRecord 积分记录
     * @return 积分记录集合
     */
    public List<PointsRecord> selectPointsRecordList(PointsRecord pointsRecord);

    /**
     * 新增积分记录
     * 
     * @param pointsRecord 积分记录
     * @return 结果
     */
    public int insertPointsRecord(PointsRecord pointsRecord);

    /**
     * 修改积分记录
     * 
     * @param pointsRecord 积分记录
     * @return 结果
     */
    public int updatePointsRecord(PointsRecord pointsRecord);

    /**
     * 删除积分记录
     * 
     * @param id 积分记录主键
     * @return 结果
     */
    public int deletePointsRecordById(Long id);

    /**
     * 批量删除积分记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsRecordByIds(Long[] ids);
}
