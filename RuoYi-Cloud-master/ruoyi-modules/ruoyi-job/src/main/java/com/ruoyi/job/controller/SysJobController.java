package com.ruoyi.job.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.security.annotation.Logical;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.exception.job.TaskException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.job.domain.SysJob;
import com.ruoyi.job.service.ISysJobService;
import com.ruoyi.job.util.CronUtils;
import com.ruoyi.job.util.ScheduleUtils;

/**
 * 调度任务信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/job")
public class SysJobController extends BaseController
{
    @Autowired
    private ISysJobService jobService;


    @RequiresPermissions(value = {"monitor:job:remove", "meeting:meeting:remove"}, logical = Logical.OR)
    @DeleteMapping("/removeByMeetingId")
    public AjaxResult removeByMeetingId(@RequestParam("meetingId") Long meetingId) throws Exception
    {
        SysJob sysBeginJob = jobService.selectBeginJobByInvoketarget(meetingId);
        SysJob sysEndJob = jobService.selectEndJobByInvoketarget(meetingId);
        // 一个meeting会创建两个任务，分别是自动开始和结束
        // 所以销毁会议的时候需要把两个子任务都删除掉
        Long[] ids = {sysBeginJob.getJobId(), sysEndJob.getJobId()};
        jobService.deleteJobByIds(ids);
        return success();
    }

    /**
     * 通过feign接口调用的修改，只会更新时间！
     * @param meetingId
     * @param time
     * @param type
     * @return
     * @throws Exception
     */
    @RequiresPermissions(value = {"monitor:job:edit", "meeting:meeting:edit"}, logical = Logical.OR)
    @PutMapping("/editByMeetingId")
    public AjaxResult editByMeetingId(
            @RequestParam("meetingId") Long meetingId,
            @RequestParam("time") String time,
            @RequestParam("type") String type) throws Exception {
        SysJob updateElem = null;
        if (type.equals("begin")) {
            updateElem = jobService.selectBeginJobByInvoketarget(meetingId);
        }
        else {
            updateElem = jobService.selectEndJobByInvoketarget(meetingId);
        }
        SysJob sysJob = new SysJob();
        sysJob.setJobId(updateElem.getJobId());
        sysJob.setCronExpression(time);
        // 状态和数据库保持一致
        sysJob.setStatus(updateElem.getStatus());

        if (!CronUtils.isValid(sysJob.getCronExpression()))
        {
            return error("修改任务'" + sysJob.getJobName() + "'失败，Cron表达式不正确");
        }

        sysJob.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(jobService.updateJob(sysJob));
    }





    /**
     * 查询定时任务列表
     */
    @RequiresPermissions("monitor:job:list")
    @GetMapping("/list")
    public TableDataInfo list(SysJob sysJob)
    {
        startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        return getDataTable(list);
    }

    /**
     * 导出定时任务列表
     */
    @RequiresPermissions("monitor:job:export")
    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        util.exportExcel(response, list, "定时任务");
    }

    /**
     * 获取定时任务详细信息
     */
    @RequiresPermissions("monitor:job:query")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return success(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     * 直接在管理页面中可以插入
     * 同时，在设置会议的时候要可以同步插入
     */
    @RequiresPermissions(value = {"monitor:job:add", "meeting:meeting:add"}, logical = Logical.OR)
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        System.err.println(job.getCronExpression());
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS }))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setCreateBy(SecurityUtils.getUsername());
        return toAjax(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     */
    @RequiresPermissions("monitor:job:edit")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS }))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget()))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        job.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @RequiresPermissions("monitor:job:changeStatus")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @RequiresPermissions("monitor:job:changeStatus")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException
    {
        boolean result = jobService.run(job);
        return result ? success() : error("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @RequiresPermissions("monitor:job:remove")
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        jobService.deleteJobByIds(jobIds);
        return success();
    }
}
