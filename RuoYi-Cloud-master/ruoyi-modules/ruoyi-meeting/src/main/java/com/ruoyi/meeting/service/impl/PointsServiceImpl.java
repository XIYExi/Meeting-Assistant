package com.ruoyi.meeting.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.meeting.domain.PointsRecord;
import com.ruoyi.meeting.domain.PointsWallet;
import com.ruoyi.meeting.entity.PointResponse;
import com.ruoyi.meeting.entity.PointsRecordResponse;
import com.ruoyi.meeting.mapper.PointsRecordMapper;
import com.ruoyi.meeting.mapper.PointsWalletMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.meeting.mapper.PointsMapper;
import com.ruoyi.meeting.domain.Points;
import com.ruoyi.meeting.service.IPointsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 积分Service业务层处理
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Service
@Transactional
public class PointsServiceImpl implements IPointsService 
{
    @Autowired
    private PointsMapper pointsMapper;
    @Autowired
    private PointsWalletMapper pointsWalletMapper;
    @Autowired
    private PointsRecordMapper pointsRecordMapper;

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

    @Override
    public PointsWallet selectUserWalletById(Long userId) {
        return pointsWalletMapper.selectUserWalletById(userId);
    }

    @Override
    public List<PointResponse> selectUserPointList(Long userId) {
        // 当前用户完成的 有记录的任务
        PointsRecord pointsRecordQuery = new PointsRecord();
        pointsRecordQuery.setUserId(userId);
        List<PointsRecord> pointsRecords = pointsRecordMapper.selectPointsRecordList(pointsRecordQuery);

        // 所有任务列表
        List<Points> points = pointsMapper.selectPointsList(new Points());

        // 便利任务列表，然后判断当天是否可以再次完成
        List<PointResponse> collect = points.stream().map(point -> {
            PointResponse pointResponse = new PointResponse();
            BeanUtils.copyProperties(point, pointResponse);

            // 判断canComplete的值是什么
            if (Objects.equals(point.getType(), 1L)) {
                // 只能做一次，判断数据库里面有没有就行了
                pointsRecordQuery.setUserId(userId);
                pointsRecordQuery.setPointId(point.getId());
                List<PointsRecord> userRecordList = pointsRecordMapper.selectPointsRecordList(pointsRecordQuery);
                if (!userRecordList.isEmpty())
                    pointResponse.setCanComplete(false);
                else
                    pointResponse.setCanComplete(true);
            } else {
                // 每天一次，再判断有没有的基础之上判断，updateTime是否是当日
                List<PointsRecord> userRecordListDaily = pointsRecordMapper.selectUserDailyCompleteList(userId, point.getId());
                if (!userRecordListDaily.isEmpty())
                    pointResponse.setCanComplete(false);
                else
                    pointResponse.setCanComplete(true);
            }

            return pointResponse;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public boolean submitTaskForPoint(Long pointId, Long userId) {
        Points points = pointsMapper.selectPointsById(pointId);
        Long type = points.getType();
        if (Objects.equals(type, 1L)) {
            // 只能完成一次，判断数据库里面有没有
            PointsRecord pointsRecordQuery = new PointsRecord();
            pointsRecordQuery.setPointId(pointId);
            pointsRecordQuery.setUserId(userId);
            List<PointsRecord> pointsRecords = pointsRecordMapper.selectPointsRecordList(pointsRecordQuery);
            if (pointsRecords.isEmpty()) {
                PointsRecord pointsRecordOnce = new PointsRecord();
                pointsRecordOnce.setUserId(userId);
                pointsRecordOnce.setPointId(pointId);
                pointsRecordOnce.setGetPoints(points.getPoint());
                pointsRecordOnce.setCreateTime(DateUtils.getNowDate());
                pointsRecordOnce.setUpdateTime(DateUtils.getNowDate());
                pointsRecordMapper.insertPointsRecord(pointsRecordOnce);
                // 给用户加积分
                PointsWallet pointsWallet = pointsWalletMapper.selectUserWalletById(userId);
                pointsWallet.setTotal(pointsWallet.getTotal() + points.getPoint());
                pointsWalletMapper.updatePointsWallet(pointsWallet);
                return true;
            }
            else
                return false;
        }
        else {
            // 每天可以完成一次的任务
            List<PointsRecord> pointsRecordsDaily = pointsRecordMapper.selectUserDailyCompleteList(userId, pointId);
            if (pointsRecordsDaily.isEmpty()) {
                // 不存在数据 今天没有插入数据 直接插入
                PointsRecord pointsRecordDaily = new PointsRecord();
                pointsRecordDaily.setUserId(userId);
                pointsRecordDaily.setPointId(pointId);
                pointsRecordDaily.setGetPoints(points.getPoint());
                pointsRecordDaily.setCreateTime(DateUtils.getNowDate());
                pointsRecordDaily.setUpdateTime(DateUtils.getNowDate());
                pointsRecordMapper.insertPointsRecord(pointsRecordDaily);

                PointsWallet pointsWallet = pointsWalletMapper.selectUserWalletById(userId);
                pointsWallet.setTotal(pointsWallet.getTotal() + points.getPoint());
                pointsWalletMapper.updatePointsWallet(pointsWallet);
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public List<PointsRecordResponse> getHistoryPointRecords(Long userId) {
        List<PointsRecordResponse> list = pointsMapper.selectHistoryPointsRecord(userId);
        return list;
    }

    @Override
    public boolean dailySignIn(Long userId) {
        Points pointsQuery = new Points();
        pointsQuery.setTitle("每日签到");
        pointsQuery.setType(2L);
        List<Points> points = pointsMapper.selectPointsList(pointsQuery);
        Points dailyPointElement = points.get(0);

        Long pointId = dailyPointElement.getId();
        List<PointsRecord> pointsRecords = pointsRecordMapper.selectDailySignIn(pointId);

        return pointsRecords.isEmpty();
    }
}
