package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.PointsItems;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分物品Mapper接口
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Mapper
public interface PointsItemsMapper 
{
    /**
     * 查询积分物品
     * 
     * @param id 积分物品主键
     * @return 积分物品
     */
    public PointsItems selectPointsItemsById(Long id);

    /**
     * 查询积分物品列表
     * 
     * @param pointsItems 积分物品
     * @return 积分物品集合
     */
    public List<PointsItems> selectPointsItemsList(PointsItems pointsItems);

    /**
     * 新增积分物品
     * 
     * @param pointsItems 积分物品
     * @return 结果
     */
    public int insertPointsItems(PointsItems pointsItems);

    /**
     * 修改积分物品
     * 
     * @param pointsItems 积分物品
     * @return 结果
     */
    public int updatePointsItems(PointsItems pointsItems);

    /**
     * 删除积分物品
     * 
     * @param id 积分物品主键
     * @return 结果
     */
    public int deletePointsItemsById(Long id);

    /**
     * 批量删除积分物品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsItemsByIds(Long[] ids);
}
