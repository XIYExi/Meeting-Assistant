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
import com.ruoyi.meeting.domain.MeetingActivity;
import com.ruoyi.meeting.service.IMeetingActivityService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

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

    /**
     * 新增会议活动
     */
    @RequiresPermissions("meeting:activity:add")
    @Log(title = "会议活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeetingActivity meetingActivity)
    {
        return toAjax(meetingActivityService.insertMeetingActivity(meetingActivity));
    }

    /**
     * 修改会议活动
     */
    @RequiresPermissions("meeting:activity:edit")
    @Log(title = "会议活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MeetingActivity meetingActivity)
    {
        return toAjax(meetingActivityService.updateMeetingActivity(meetingActivity));
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
