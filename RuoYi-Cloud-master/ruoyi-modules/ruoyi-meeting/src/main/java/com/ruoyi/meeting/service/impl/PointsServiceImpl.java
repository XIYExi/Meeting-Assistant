package com.ruoyi.meeting.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.PointsMapper;
import com.ruoyi.meeting.domain.Points;
import com.ruoyi.meeting.service.IPointsService;

/**
 * 积分Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Service
public class PointsServiceImpl implements IPointsService 
{
    @Autowired
    private PointsMapper pointsMapper;

    /**
     * 查询积分
     * 
     * @param id 积分主键
     * @return 积分
     */
    @Override
    public Points selectPointsById(Long id)
    {
        return pointsMapper.selectPointsById(id);
    }

    /**
     * 查询积分列表
     * 
     * @param points 积分
     * @return 积分
     */
    @Override
    public List<Points> selectPointsList(Points points)
    {
        return pointsMapper.selectPointsList(points);
    }

    /**
     * 新增积分
     * 
     * @param points 积分
     * @return 结果
     */
    @Override
    public int insertPoints(Points points)
    {
        points.setCreateTime(DateUtils.getNowDate());
        return pointsMapper.insertPoints(points);
    }

    /**
     * 修改积分
     * 
     * @param points 积分
     * @return 结果
     */
    @Override
    public int updatePoints(Points points)
    {
        points.setUpdateTime(DateUtils.getNowDate());
        return pointsMapper.updatePoints(points);
    }

    /**
     * 批量删除积分
     * 
     * @param ids 需要删除的积分主键
     * @return 结果
     */
    @Override
    public int deletePointsByIds(Long[] ids)
    {
        return pointsMapper.deletePointsByIds(ids);
    }

    /**
     * 删除积分信息
     * 
     * @param id 积分主键
     * @return 结果
     */
    @Override
    public int deletePointsById(Long id)
    {
        return pointsMapper.deletePointsById(id);
    }
}
