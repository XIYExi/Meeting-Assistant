package com.ruoyi.meeting.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
