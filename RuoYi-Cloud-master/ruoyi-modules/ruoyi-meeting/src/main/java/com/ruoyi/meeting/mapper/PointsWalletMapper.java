package com.ruoyi.meeting.mapper;

import java.util.List;
import com.ruoyi.meeting.domain.PointsWallet;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户积分Mapper接口
 * 
 * @author xiye
 * @date 2025-02-24
 */
@Mapper
public interface PointsWalletMapper 
{
    /**
     * 查询用户积分
     * 
     * @param id 用户积分主键
     * @return 用户积分
     */
    public PointsWallet selectPointsWalletById(Long id);

    /**
     * 查询用户积分列表
     * 
     * @param pointsWallet 用户积分
     * @return 用户积分集合
     */
    public List<PointsWallet> selectPointsWalletList(PointsWallet pointsWallet);

    /**
     * 新增用户积分
     * 
     * @param pointsWallet 用户积分
     * @return 结果
     */
    public int insertPointsWallet(PointsWallet pointsWallet);

    /**
     * 修改用户积分
     * 
     * @param pointsWallet 用户积分
     * @return 结果
     */
    public int updatePointsWallet(PointsWallet pointsWallet);

    /**
     * 删除用户积分
     * 
     * @param id 用户积分主键
     * @return 结果
     */
    public int deletePointsWalletById(Long id);

    /**
     * 批量删除用户积分
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsWalletByIds(Long[] ids);
}
