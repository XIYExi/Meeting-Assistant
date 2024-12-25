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
import com.ruoyi.meeting.domain.MeetingGuest;
import com.ruoyi.meeting.service.IMeetingGuestService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 会议嘉宾Controller
 * 
 * @author xiye
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/guest")
public class MeetingGuestController extends BaseController
{
    @Autowired
    private IMeetingGuestService meetingGuestService;

    /**
     * 查询会议嘉宾列表
     */
    @RequiresPermissions("meeting:guest:list")
    @GetMapping("/list")
    public TableDataInfo list(MeetingGuest meetingGuest)
    {
        startPage();
        List<MeetingGuest> list = meetingGuestService.selectMeetingGuestList(meetingGuest);
        return getDataTable(list);
    }

    /**
     * 导出会议嘉宾列表
     */
    @RequiresPermissions("meeting:guest:export")
    @Log(title = "会议嘉宾", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MeetingGuest meetingGuest)
    {
        List<MeetingGuest> list = meetingGuestService.selectMeetingGuestList(meetingGuest);
        ExcelUtil<MeetingGuest> util = new ExcelUtil<MeetingGuest>(MeetingGuest.class);
        util.exportExcel(response, list, "会议嘉宾数据");
    }

    /**
     * 获取会议嘉宾详细信息
     */
    @RequiresPermissions("meeting:guest:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(meetingGuestService.selectMeetingGuestById(id));
    }

    /**
     * 新增会议嘉宾
     */
    @RequiresPermissions("meeting:guest:add")
    @Log(title = "会议嘉宾", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeetingGuest meetingGuest)
    {
        return toAjax(meetingGuestService.insertMeetingGuest(meetingGuest));
    }

    /**
     * 修改会议嘉宾
     */
    @RequiresPermissions("meeting:guest:edit")
    @Log(title = "会议嘉宾", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MeetingGuest meetingGuest)
    {
        return toAjax(meetingGuestService.updateMeetingGuest(meetingGuest));
    }

    /**
     * 删除会议嘉宾
     */
    @RequiresPermissions("meeting:guest:remove")
    @Log(title = "会议嘉宾", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingGuestService.deleteMeetingGuestByIds(ids));
    }
}
