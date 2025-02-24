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
import com.ruoyi.meeting.domain.PointsItems;
import com.ruoyi.meeting.service.IPointsItemsService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 积分物品Controller
 * 
 * @author xiye
 * @date 2025-02-24
 */
@RestController
@RequestMapping("/items")
public class PointsItemsController extends BaseController
{
    @Autowired
    private IPointsItemsService pointsItemsService;

    /**
     * 查询积分物品列表
     */
    @RequiresPermissions("meeting:items:list")
    @GetMapping("/list")
    public TableDataInfo list(PointsItems pointsItems)
    {
        startPage();
        List<PointsItems> list = pointsItemsService.selectPointsItemsList(pointsItems);
        return getDataTable(list);
    }

    /**
     * 导出积分物品列表
     */
    @RequiresPermissions("meeting:items:export")
    @Log(title = "积分物品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointsItems pointsItems)
    {
        List<PointsItems> list = pointsItemsService.selectPointsItemsList(pointsItems);
        ExcelUtil<PointsItems> util = new ExcelUtil<PointsItems>(PointsItems.class);
        util.exportExcel(response, list, "积分物品数据");
    }

    /**
     * 获取积分物品详细信息
     */
    @RequiresPermissions("meeting:items:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(pointsItemsService.selectPointsItemsById(id));
    }

    /**
     * 新增积分物品
     */
    @RequiresPermissions("meeting:items:add")
    @Log(title = "积分物品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointsItems pointsItems)
    {
        return toAjax(pointsItemsService.insertPointsItems(pointsItems));
    }

    /**
     * 修改积分物品
     */
    @RequiresPermissions("meeting:items:edit")
    @Log(title = "积分物品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointsItems pointsItems)
    {
        return toAjax(pointsItemsService.updatePointsItems(pointsItems));
    }

    /**
     * 删除积分物品
     */
    @RequiresPermissions("meeting:items:remove")
    @Log(title = "积分物品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pointsItemsService.deletePointsItemsByIds(ids));
    }
}
