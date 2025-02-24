package com.ruoyi.meeting.service;

import java.util.List;
import com.ruoyi.meeting.domain.Points;

/**
 * 积分Service接口
 * 
 * @author xiye
 * @date 2025-02-24
 */
public interface IPointsService 
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
     * 批量删除积分
     * 
     * @param ids 需要删除的积分主键集合
     * @return 结果
     */
    public int deletePointsByIds(Long[] ids);

    /**
     * 删除积分信息
     * 
     * @param id 积分主键
     * @return 结果
     */
    public int deletePointsById(Long id);
}
