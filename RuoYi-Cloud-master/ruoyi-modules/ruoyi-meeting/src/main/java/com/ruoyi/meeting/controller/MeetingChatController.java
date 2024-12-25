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
import com.ruoyi.meeting.domain.MeetingChat;
import com.ruoyi.meeting.service.IMeetingChatService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 会议聊天Controller
 * 
 * @author xiye
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/chat")
public class MeetingChatController extends BaseController
{
    @Autowired
    private IMeetingChatService meetingChatService;

    /**
     * 查询会议聊天列表
     */
    @RequiresPermissions("meeting:chat:list")
    @GetMapping("/list")
    public TableDataInfo list(MeetingChat meetingChat)
    {
        startPage();
        List<MeetingChat> list = meetingChatService.selectMeetingChatList(meetingChat);
        return getDataTable(list);
    }

    /**
     * 导出会议聊天列表
     */
    @RequiresPermissions("meeting:chat:export")
    @Log(title = "会议聊天", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MeetingChat meetingChat)
    {
        List<MeetingChat> list = meetingChatService.selectMeetingChatList(meetingChat);
        ExcelUtil<MeetingChat> util = new ExcelUtil<MeetingChat>(MeetingChat.class);
        util.exportExcel(response, list, "会议聊天数据");
    }

    /**
     * 获取会议聊天详细信息
     */
    @RequiresPermissions("meeting:chat:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(meetingChatService.selectMeetingChatById(id));
    }

    /**
     * 新增会议聊天
     */
    @RequiresPermissions("meeting:chat:add")
    @Log(title = "会议聊天", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeetingChat meetingChat)
    {
        return toAjax(meetingChatService.insertMeetingChat(meetingChat));
    }

    /**
     * 修改会议聊天
     */
    @RequiresPermissions("meeting:chat:edit")
    @Log(title = "会议聊天", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MeetingChat meetingChat)
    {
        return toAjax(meetingChatService.updateMeetingChat(meetingChat));
    }

    /**
     * 删除会议聊天
     */
    @RequiresPermissions("meeting:chat:remove")
    @Log(title = "会议聊天", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(meetingChatService.deleteMeetingChatByIds(ids));
    }
}
