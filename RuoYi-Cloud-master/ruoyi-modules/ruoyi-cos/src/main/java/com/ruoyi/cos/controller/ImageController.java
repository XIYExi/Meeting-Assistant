package com.ruoyi.cos.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cos.domain.Image;
import com.ruoyi.cos.service.IImageService;
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
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 图片cosController
 * 
 * @author xiye
 * @date 2024-12-26
 */
@RestController
@RequestMapping("/image")
public class ImageController extends BaseController
{
    @Autowired
    private IImageService imageService;

    /**
     * 查询图片cos列表
     */
    @RequiresPermissions("meeting:image:list")
    @GetMapping("/list")
    public TableDataInfo list(Image image)
    {
        startPage();
        List<Image> list = imageService.selectImageList(image);
        return getDataTable(list);
    }

    /**
     * 导出图片cos列表
     */
    @RequiresPermissions("meeting:image:export")
    @Log(title = "图片cos", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Image image)
    {
        List<Image> list = imageService.selectImageList(image);
        ExcelUtil<Image> util = new ExcelUtil<Image>(Image.class);
        util.exportExcel(response, list, "图片cos数据");
    }

    /**
     * 获取图片cos详细信息
     */
    @RequiresPermissions("meeting:image:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(imageService.selectImageById(id));
    }

    /**
     * 新增图片cos
     */
    @RequiresPermissions("meeting:image:add")
    @Log(title = "图片cos", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Image image)
    {
        return toAjax(imageService.insertImage(image));
    }

    /**
     * 修改图片cos
     */
    @RequiresPermissions("meeting:image:edit")
    @Log(title = "图片cos", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Image image)
    {
        return toAjax(imageService.updateImage(image));
    }

    /**
     * 删除图片cos
     */
    @RequiresPermissions("meeting:image:remove")
    @Log(title = "图片cos", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(imageService.deleteImageByIds(ids));
    }
}
