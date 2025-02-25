package com.ruoyi.meeting.service.impl;

import java.util.Arrays;
import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.cos.api.RemoteCosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.PointsItemsMapper;
import com.ruoyi.meeting.domain.PointsItems;
import com.ruoyi.meeting.service.IPointsItemsService;

import javax.annotation.Resource;

/**
 * 积分物品Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Service
public class PointsItemsServiceImpl implements IPointsItemsService 
{
    private static final Logger logger = LoggerFactory.getLogger(PointsItemsServiceImpl.class);

    @Autowired
    private PointsItemsMapper pointsItemsMapper;
    @Resource
    private RemoteCosService remoteCosService;

    /**
     * 查询积分物品
     * 
     * @param id 积分物品主键
     * @return 积分物品
     */
    @Override
    public PointsItems selectPointsItemsById(Long id)
    {
        return pointsItemsMapper.selectPointsItemsById(id);
    }

    /**
     * 查询积分物品列表
     * 
     * @param pointsItems 积分物品
     * @return 积分物品
     */
    @Override
    public List<PointsItems> selectPointsItemsList(PointsItems pointsItems)
    {
        return pointsItemsMapper.selectPointsItemsList(pointsItems);
    }

    /**
     * 新增积分物品
     * 
     * @param pointsItems 积分物品
     * @return 结果
     */
    @Override
    public int insertPointsItems(PointsItems pointsItems)
    {
        pointsItems.setCreateTime(DateUtils.getNowDate());
        return pointsItemsMapper.insertPointsItems(pointsItems);
    }

    /**
     * 修改积分物品
     * 
     * @param pointsItems 积分物品
     * @return 结果
     */
    @Override
    public int updatePointsItems(PointsItems pointsItems)
    {
        pointsItems.setUpdateTime(DateUtils.getNowDate());
        return pointsItemsMapper.updatePointsItems(pointsItems);
    }

    /**
     * 批量删除积分物品
     * 
     * @param ids 需要删除的积分物品主键
     * @return 结果
     */
    @Override
    public int deletePointsItemsByIds(Long[] ids)
    {
        Arrays.stream(ids).forEach(pointItemId -> {
            PointsItems pointsItems = pointsItemsMapper.selectPointsItemsById(pointItemId);
            String url = pointsItems.getUrl();
            if (!url.equals("null")) {
                remoteCosService.removeImage(url);
            }
        });
        return pointsItemsMapper.deletePointsItemsByIds(ids);
    }

    /**
     * 删除积分物品信息
     * 
     * @param id 积分物品主键
     * @return 结果
     */
    @Override
    public int deletePointsItemsById(Long id)
    {
        PointsItems pointsItems = pointsItemsMapper.selectPointsItemsById(id);
        String url = pointsItems.getUrl();
        if (!url.equals("null")) {
            remoteCosService.removeImage(url);
        }
        return pointsItemsMapper.deletePointsItemsById(id);
    }
}
