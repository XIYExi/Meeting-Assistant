package com.ruoyi.meeting.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.meeting.domain.MeetingAgenda;
import com.ruoyi.meeting.service.IMeetingAgendaService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 会议议程Controller
 * 
 * @author xiye
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/agenda")
public class MeetingAgendaController extends BaseController
{
    @Autowired
    private IMeetingAgendaService meetingAgendaService;

    /**
     * 查询会议议程列表
     */
    @RequiresPermissions("meeting:agenda:list")
    @GetMapping("/list")
    public TableDataInfo list(MeetingAgenda meetingAgenda)
    {
        startPage();
        List<MeetingAgenda> list = meetingAgendaService.selectMeetingAgendaList(meetingAgenda);
        return getDataTable(list);
    }

    /**
     * 导出会议议程列表
     */
    @RequiresPermissions("meeting:agenda:export")
    @Log(title = "会议议程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MeetingAgenda meetingAgenda)
    {
        List<MeetingAgenda> list = meetingAgendaService.selectMeetingAgendaList(meetingAgenda);
        ExcelUtil<MeetingAgenda> util = new ExcelUtil<MeetingAgenda>(MeetingAgenda.class);
        util.exportExcel(response, list, "会议议程数据");
    }

    /**
     * 根据meetingId获取会议议程详细信息
     */
    @GetMapping(value = "/details")
    public AjaxResult getInfoByMeetingId(@RequestParam(value = "id") Long id)
    {
        return success(meetingAgendaService.selectMeetingAgendaByMeetingId(id));
    }


    /**
     * 获取会议议程详细信息
     */
    @RequiresPermissions("meeting:agenda:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(meetingAgendaService.selectMeetingAgendaById(id));
    }

    /**
     * 新增会议议程
     */
    @RequiresPermissions("meeting:agenda:add")
    @Log(title = "会议议程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeetingAgenda meetingAgenda)
    {
        return toAjax(meetingAgendaService.insertMeetingAgenda(meetingAgenda));
    }

    /**
     * 修改会议议程
     */
    @RequiresPermissions("meeting:agenda:edit")
    @Log(title = "会议议程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MeetingAgenda meetingAgenda)
    {
        return toAjax(meetingAgendaService.updateMeetingAgenda(meetingAgenda));
    }

    /**
     * 删除会议议程
     */
    @RequiresPermissions("meeting:agenda:remove")
    @Log(title = "会议议程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingAgendaService.deleteMeetingAgendaByIds(ids));
    }
}
