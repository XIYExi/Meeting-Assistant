package com.ruoyi.meeting.controller;

import java.util.List;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cos.api.RemoteCosService;
import com.ruoyi.meeting.constant.CosConstant;
import com.ruoyi.meeting.domain.News;
import com.ruoyi.meeting.entity.MeetingActivityRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.MeetingActivity;
import com.ruoyi.meeting.service.IMeetingActivityService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 会议活动Controller
 * 
 * @author xiye
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/activity")
public class MeetingActivityController extends BaseController
{
    @Autowired
    private IMeetingActivityService meetingActivityService;
    @Resource
    private RemoteCosService remoteCosService;

    /**
     * 查询会议活动列表
     */
    @RequiresPermissions("meeting:activity:list")
    @GetMapping("/list")
    public TableDataInfo list(MeetingActivity meetingActivity)
    {
        startPage();
        List<MeetingActivity> list = meetingActivityService.selectMeetingActivityList(meetingActivity);
        return getDataTable(list);
    }

    /**
     * 导出会议活动列表
     */
    @RequiresPermissions("meeting:activity:export")
    @Log(title = "会议活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MeetingActivity meetingActivity)
    {
        List<MeetingActivity> list = meetingActivityService.selectMeetingActivityList(meetingActivity);
        ExcelUtil<MeetingActivity> util = new ExcelUtil<MeetingActivity>(MeetingActivity.class);
        util.exportExcel(response, list, "会议活动数据");
    }

    /**
     * 获取会议活动详细信息
     */
    @RequiresPermissions("meeting:activity:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(meetingActivityService.selectMeetingActivityById(id));
    }

    @RequiresPermissions("meeting:activity:add")
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
     * 新增会议活动
     */
    @RequiresPermissions("meeting:activity:add")
    @Log(title = "会议活动", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody MeetingActivity meetingActivity)
    {
        return toAjax(meetingActivityService.insertMeetingActivity(meetingActivity));
    }

    /**
     * 修改会议活动
     */
    @RequiresPermissions("meeting:activity:edit")
    @Log(title = "会议活动", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(MeetingActivityRequest meetingActivityRequest)
    {
        int updateMeetingActivityItem = 0;
        // 如果传了图片就调用然后更新
        if (meetingActivityRequest.getFile() != null) {
            if (!meetingActivityRequest.getFile().isEmpty()) {
                AjaxResult ajaxResult = remoteCosService.uploadFileSystem(meetingActivityRequest.getFile(), meetingActivityRequest.getImageId());
                if (ajaxResult.get("code").toString().equals("200")) {
                    // 走到这里image库里面插入了一条新的数据，现在删除老的图片
                    if (meetingActivityRequest.getUrl().startsWith("https")) {
                        // 如果url开头，那么就说明开始的时候插入了数据，需要去Image库里面删除
                        remoteCosService.removeImage(meetingActivityRequest.getUrl());
                    }
                }
            }
        }
        MeetingActivity meetingActivity = new MeetingActivity();
        BeanUtils.copyProperties(meetingActivityRequest, meetingActivity);

        if (meetingActivityRequest.getFile() != null) {
            String filename = meetingActivityRequest.getFile().getOriginalFilename();
            String extend = filename.substring(filename.lastIndexOf(".") + 1);
            if (!meetingActivityRequest.getFile().isEmpty())
                meetingActivity.setUrl(CosConstant.COS_PATH + "article/" + meetingActivityRequest.getImageId() + "." + extend);
        }
        updateMeetingActivityItem = meetingActivityService.updateMeetingActivity(meetingActivity);
        return toAjax(updateMeetingActivityItem == 1);
    }

    /**
     * 删除会议活动
     */
    @RequiresPermissions("meeting:activity:remove")
    @Log(title = "会议活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingActivityService.deleteMeetingActivityByIds(ids));
    }
}
