package com.ruoyi.meeting.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.meeting.domain.PointsRecord;
import com.ruoyi.meeting.domain.PointsWallet;
import com.ruoyi.meeting.entity.PointResponse;
import com.ruoyi.meeting.entity.PointsRecordResponse;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.Points;
import com.ruoyi.meeting.service.IPointsService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 积分Controller
 * 
 * @author xiye
 * @date 2025-02-24
 */
@RestController
@RequestMapping("/points")
public class PointsController extends BaseController
{
    @Autowired
    private IPointsService pointsService;

    /**
     * 查询个人积分
     * @param userId
     * @return
     */
    @GetMapping("/wallet")
    public AjaxResult wallet(@RequestParam("userId") Long userId) {
        PointsWallet pointsWallet = pointsService.selectUserWalletById(userId);
        return AjaxResult.success(pointsWallet);
    }


    /**
     * 查询当前用户可以完成的任务列表
     * @param userId
     * @return
     */
    @GetMapping("/getUserPointList")
    public AjaxResult getUserPointList(@RequestParam("userId") Long userId) {
        List<PointResponse> pointResponseList = pointsService.selectUserPointList(userId);
        return AjaxResult.success(pointResponseList);
    }


    @GetMapping("/submitTaskForPoint")
    public AjaxResult submitTaskForPoint(@RequestParam("pointId") Long pointId, @RequestParam("userId") Long userId) {
        boolean result = pointsService.submitTaskForPoint(pointId, userId);
        return result ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 获得历史积分获取列表
     * @param userId
     * @return
     */
    @GetMapping("/getHistoryPointRecords")
    public AjaxResult getHistoryPointRecords(@RequestParam("userId") Long userId) {
        List<PointsRecordResponse> recordReponse = pointsService.getHistoryPointRecords(userId);
        return AjaxResult.success(recordReponse);
    }

    /**
     * 查询每日打卡是否完成
     * @param userId
     * @return
     */
    @GetMapping("/dailySignIn")
    public AjaxResult dailySignIn(@RequestParam("userId") Long userId) {
        boolean isSignIn = pointsService.dailySignIn(userId);
        return AjaxResult.success(isSignIn);
    }


    /**
     * 查询积分列表
     */
    @RequiresPermissions("meeting:points:list")
    @GetMapping("/list")
    public TableDataInfo list(Points points)
    {
        startPage();
        List<Points> list = pointsService.selectPointsList(points);
        return getDataTable(list);
    }

    /**
     * 导出积分列表
     */
    @RequiresPermissions("meeting:points:export")
    @Log(title = "积分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Points points)
    {
        List<Points> list = pointsService.selectPointsList(points);
        ExcelUtil<Points> util = new ExcelUtil<Points>(Points.class);
        util.exportExcel(response, list, "积分数据");
    }

    /**
     * 获取积分详细信息
     */
    @RequiresPermissions("meeting:points:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pointsService.selectPointsById(id));
    }

    /**
     * 新增积分
     */
    @RequiresPermissions("meeting:points:add")
    @Log(title = "积分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Points points)
    {
        return toAjax(pointsService.insertPoints(points));
    }

    /**
     * 修改积分
     */
    @RequiresPermissions("meeting:points:edit")
    @Log(title = "积分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Points points)
    {
        return toAjax(pointsService.updatePoints(points));
    }

    /**
     * 删除积分
     */
    @RequiresPermissions("meeting:points:remove")
    @Log(title = "积分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pointsService.deletePointsByIds(ids));
    }
}
