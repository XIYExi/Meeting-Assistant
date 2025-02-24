package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.PointsExchange;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户积分Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-24
 */
@Mapper
public interface PointsExchangeMapper 
{
    /**
     * 查询用户积分
     * 
     * @param id 用户积分主键
     * @return 用户积分
     */
    public PointsExchange selectPointsExchangeById(Long id);

    /**
     * 查询用户积分列表
     * 
     * @param pointsExchange 用户积分
     * @return 用户积分集合
     */
    public List<PointsExchange> selectPointsExchangeList(PointsExchange pointsExchange);

    /**
     * 新增用户积分
     * 
     * @param pointsExchange 用户积分
     * @return 结果
     */
    public int insertPointsExchange(PointsExchange pointsExchange);

    /**
     * 修改用户积分
     * 
     * @param pointsExchange 用户积分
     * @return 结果
     */
    public int updatePointsExchange(PointsExchange pointsExchange);

    /**
     * 删除用户积分
     * 
     * @param id 用户积分主键
     * @return 结果
     */
    public int deletePointsExchangeById(Long id);

    /**
     * 批量删除用户积分
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsExchangeByIds(Long[] ids);
}
