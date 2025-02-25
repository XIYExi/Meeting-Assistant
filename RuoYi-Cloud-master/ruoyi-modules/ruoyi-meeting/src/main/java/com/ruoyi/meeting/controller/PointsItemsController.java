package com.ruoyi.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.constant.CosConstant;
import com.ruoyi.meeting.entity.PointItemRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.PointsItems;
import com.ruoyi.meeting.service.IPointsItemsService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

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
    @Resource
    private RemoteCosService remoteCosService;

    /**
     * 查询积分物品列表
     */
    // @RequiresPermissions("meeting:items:list")
    @GetMapping("/list")
    public TableDataInfo list(PointsItems pointsItems)
    {
        startPage();
        List<PointsItems> list = pointsItemsService.selectPointsItemsList(pointsItems);
        return getDataTable(list);
    }

    @GetMapping("/itemExchange")
    public AjaxResult itemExchange(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId) {
        boolean exchange = pointsItemsService.itemExchange(userId, itemId);
        return exchange ? AjaxResult.success() : AjaxResult.error();
    }

    @RequiresPermissions("meeting:items:add")
    @PostMapping("/addImage")
    public AjaxResult add(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam("imageId") String imageId) {
        String url = null;
        if (file != null) {
            if (!file.isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSystem(file, imageId);
                if (ajaxResult.get("code").toString().equals("200")) {
                    String filename = file.getOriginalFilename();
                    String extend = filename.substring(filename.lastIndexOf(".") + 1);
                    url = CosConstant.COS_PATH + "common/" + imageId + "." + extend;
                }
            }
        }
        return AjaxResult.success(url);
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
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PointsItems pointsItems)
    {

        return toAjax(pointsItemsService.insertPointsItems(pointsItems));
    }

    /**
     * 修改积分物品
     */
    @RequiresPermissions("meeting:items:edit")
    @Log(title = "积分物品", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(PointItemRequest pointItemRequest)
    {
        int updatePointItem = 0;
        // 如果传了图片就调用然后更新
        if (pointItemRequest.getFile() != null) {
            if (!pointItemRequest.getFile().isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSystem(pointItemRequest.getFile(), pointItemRequest.getImageId());
                if (ajaxResult.get("code").toString().equals("200")) {
                    // 走到这里image库里面插入了一条新的数据，现在删除老的图片
                    if (pointItemRequest.getUrl().startsWith("https")) {
                        // 如果url开头，那么就说明开始的时候插入了数据，需要去Image库里面删除
                        remoteCosService.removeImage(pointItemRequest.getUrl());
                    }
                }
            }
        }
        PointsItems pointsItems = new PointsItems();
        BeanUtils.copyProperties(pointItemRequest, pointsItems);

        if (pointItemRequest.getFile() != null) {
            String filename = pointItemRequest.getFile().getOriginalFilename();
            String extend = filename.substring(filename.lastIndexOf(".") + 1);
            if (!pointItemRequest.getFile().isEmpty())
                pointsItems.setUrl(CosConstant.COS_PATH + "common/" + pointItemRequest.getImageId() + "." + extend);
        }
        updatePointItem = pointsItemsService.updatePointsItems(pointsItems);
        return toAjax(updatePointItem == 1);
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
