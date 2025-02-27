package com.ruoyi.meeting.controller;

import java.util.List;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.constant.CosConstant;
import com.ruoyi.meeting.domain.News;
import com.ruoyi.meeting.entity.MeetingActivitySectorRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.MeetingActivitySector;
import com.ruoyi.meeting.service.IMeetingActivitySectorService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 会议活动板块Controller
 * 
 * @author xiye
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/sector")
public class MeetingActivitySectorController extends BaseController
{
    @Autowired
    private IMeetingActivitySectorService meetingActivitySectorService;
    @Resource
    private RemoteCosService remoteCosService;

    /**
     * 查询会议活动板块列表
     */
    @RequiresPermissions("meeting:sector:list")
    @GetMapping("/list")
    public TableDataInfo list(MeetingActivitySector meetingActivitySector)
    {
        startPage();
        List<MeetingActivitySector> list = meetingActivitySectorService.selectMeetingActivitySectorList(meetingActivitySector);
        return getDataTable(list);
    }

    @GetMapping("/getSectorList")
    public AjaxResult getSectorList() {
        List<MeetingActivitySector> meetingActivitySectors = meetingActivitySectorService.selectMeetingActivitySectorList(new MeetingActivitySector());
        return AjaxResult.success(meetingActivitySectors);
    }

    /**
     * 导出会议活动板块列表
     */
    @RequiresPermissions("meeting:sector:export")
    @Log(title = "会议活动板块", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MeetingActivitySector meetingActivitySector)
    {
        List<MeetingActivitySector> list = meetingActivitySectorService.selectMeetingActivitySectorList(meetingActivitySector);
        ExcelUtil<MeetingActivitySector> util = new ExcelUtil<MeetingActivitySector>(MeetingActivitySector.class);
        util.exportExcel(response, list, "会议活动板块数据");
    }

    /**
     * 获取会议活动板块详细信息
     */
    @RequiresPermissions("meeting:sector:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(meetingActivitySectorService.selectMeetingActivitySectorById(id));
    }


    @RequiresPermissions("meeting:sector:add")
    @PostMapping("/addImage")
    public AjaxResult addImage(@RequestPart(value = "file", required = false) MultipartFile file, @RequestParam("imageId") String imageId) {
        String url = null;
        if (file != null) {
            if (!file.isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSocial(file, imageId);
                if (ajaxResult.get("code").toString().equals("200")) {
                    String filename = file.getOriginalFilename();
                    String extend = filename.substring(filename.lastIndexOf(".") + 1);
                    url = CosConstant.COS_PATH + "article/" + imageId + "." + extend;
                }
            }
        }
        return AjaxResult.success(url);
    }
    /**
     * 新增会议活动板块
     */
    @RequiresPermissions("meeting:sector:add")
    @Log(title = "会议活动板块", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody MeetingActivitySector meetingActivitySector)
    {
        return toAjax(meetingActivitySectorService.insertMeetingActivitySector(meetingActivitySector));
    }

    /**
     * 修改会议活动板块
     */
    @RequiresPermissions("meeting:sector:edit")
    @Log(title = "会议活动板块", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(MeetingActivitySectorRequest meetingActivitySectorRequest)
    {
        int updateSectorItem = 0;
        // 如果传了图片就调用然后更新
        if (meetingActivitySectorRequest.getFile() != null) {
            if (!meetingActivitySectorRequest.getFile().isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSocial(meetingActivitySectorRequest.getFile(), meetingActivitySectorRequest.getImageId());
                if (ajaxResult.get("code").toString().equals("200")) {
                    // 走到这里image库里面插入了一条新的数据，现在删除老的图片
                    if (meetingActivitySectorRequest.getUrl().startsWith("https")) {
                        // 如果url开头，那么就说明开始的时候插入了数据，需要去Image库里面删除
                        remoteCosService.removeImage(meetingActivitySectorRequest.getUrl());
                    }
                }
            }
        }
        MeetingActivitySector meetingActivitySector = new MeetingActivitySector();
        BeanUtils.copyProperties(meetingActivitySectorRequest, meetingActivitySector);

        if (meetingActivitySectorRequest.getFile() != null) {
            String filename = meetingActivitySectorRequest.getFile().getOriginalFilename();
            String extend = filename.substring(filename.lastIndexOf(".") + 1);
            if (!meetingActivitySectorRequest.getFile().isEmpty())
                meetingActivitySector.setUrl(CosConstant.COS_PATH + "article/" + meetingActivitySectorRequest.getImageId() + "." + extend);
        }
        updateSectorItem = meetingActivitySectorService.updateMeetingActivitySector(meetingActivitySector);
        return toAjax(updateSectorItem == 1);
    }

    /**
     * 删除会议活动板块
     */
    @RequiresPermissions("meeting:sector:remove")
    @Log(title = "会议活动板块", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingActivitySectorService.deleteMeetingActivitySectorByIds(ids));
    }
}
