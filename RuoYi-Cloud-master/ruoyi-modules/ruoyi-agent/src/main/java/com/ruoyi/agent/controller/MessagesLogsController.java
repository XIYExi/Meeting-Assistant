package com.ruoyi.agent.controller;

import java.util.List;
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
import com.ruoyi.agent.domain.MessagesLogs;
import com.ruoyi.agent.service.IMessagesLogsService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * agent聊天记录Controller
 * 
 * @author xiye
 * @date 2025-01-23
 */
@RestController
@RequestMapping("/logs")
public class MessagesLogsController extends BaseController
{
    @Autowired
    private IMessagesLogsService messagesLogsService;

    /**
     * 查询agent聊天记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MessagesLogs messagesLogs)
    {
        startPage();
        List<MessagesLogs> list = messagesLogsService.selectMessagesLogsList(messagesLogs);
        return getDataTable(list);
    }

    /**
     * 导出agent聊天记录列表
     */
    @Log(title = "agent聊天记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MessagesLogs messagesLogs)
    {
        List<MessagesLogs> list = messagesLogsService.selectMessagesLogsList(messagesLogs);
        ExcelUtil<MessagesLogs> util = new ExcelUtil<MessagesLogs>(MessagesLogs.class);
        util.exportExcel(response, list, "agent聊天记录数据");
    }

    /**
     * 获取agent聊天记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(messagesLogsService.selectMessagesLogsById(id));
    }

    /**
     * 新增agent聊天记录
     */
    @Log(title = "agent聊天记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MessagesLogs messagesLogs)
    {
        return toAjax(messagesLogsService.insertMessagesLogs(messagesLogs));
    }

    /**
     * 修改agent聊天记录
     */
    @Log(title = "agent聊天记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MessagesLogs messagesLogs)
    {
        return toAjax(messagesLogsService.updateMessagesLogs(messagesLogs));
    }

    /**
     * 删除agent聊天记录
     */
    @Log(title = "agent聊天记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(messagesLogsService.deleteMessagesLogsByIds(ids));
    }
}
