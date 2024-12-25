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
import com.ruoyi.meeting.domain.Meeting;
import com.ruoyi.meeting.service.IMeetingService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 会议Controller
 * 
 * @author xiye
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/meeting")
public class MeetingController extends BaseController
{
    @Autowired
    private IMeetingService meetingService;

    /**
     * 查询会议列表
     */
    @RequiresPermissions("meeting:meeting:list")
    @GetMapping("/list")
    public TableDataInfo list(Meeting meeting)
    {
        startPage();
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        return getDataTable(list);
    }

    /**
     * 导出会议列表
     */
    @RequiresPermissions("meeting:meeting:export")
    @Log(title = "会议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Meeting meeting)
    {
        List<Meeting> list = meetingService.selectMeetingList(meeting);
        ExcelUtil<Meeting> util = new ExcelUtil<Meeting>(Meeting.class);
        util.exportExcel(response, list, "会议数据");
    }

    /**
     * 获取会议详细信息
     */
    @RequiresPermissions("meeting:meeting:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(meetingService.selectMeetingById(id));
    }

    /**
     * 新增会议
     */
    @RequiresPermissions("meeting:meeting:add")
    @Log(title = "会议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Meeting meeting)
    {
        return toAjax(meetingService.insertMeeting(meeting));
    }

    /**
     * 修改会议
     */
    @RequiresPermissions("meeting:meeting:edit")
    @Log(title = "会议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Meeting meeting)
    {
        return toAjax(meetingService.updateMeeting(meeting));
    }

    /**
     * 删除会议
     */
    @RequiresPermissions("meeting:meeting:remove")
    @Log(title = "会议", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingService.deleteMeetingByIds(ids));
    }
}
