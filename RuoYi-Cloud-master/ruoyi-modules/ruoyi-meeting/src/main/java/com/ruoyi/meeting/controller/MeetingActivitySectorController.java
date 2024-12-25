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
import com.ruoyi.meeting.domain.MeetingActivitySector;
import com.ruoyi.meeting.service.IMeetingActivitySectorService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

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

    /**
     * 新增会议活动板块
     */
    @RequiresPermissions("meeting:sector:add")
    @Log(title = "会议活动板块", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeetingActivitySector meetingActivitySector)
    {
        return toAjax(meetingActivitySectorService.insertMeetingActivitySector(meetingActivitySector));
    }

    /**
     * 修改会议活动板块
     */
    @RequiresPermissions("meeting:sector:edit")
    @Log(title = "会议活动板块", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MeetingActivitySector meetingActivitySector)
    {
        return toAjax(meetingActivitySectorService.updateMeetingActivitySector(meetingActivitySector));
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
