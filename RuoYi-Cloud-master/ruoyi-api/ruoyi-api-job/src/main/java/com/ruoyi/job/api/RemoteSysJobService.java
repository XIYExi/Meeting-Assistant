package com.ruoyi.job.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.job.api.domain.SysJob;
import com.ruoyi.job.api.factory.RemoteJobFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@FeignClient(contextId = "remoteScheduleService", value = ServiceNameConstants.JOB_SERVICE, fallbackFactory = RemoteJobFallbackFactory.class)
public interface RemoteSysJobService {

    @PostMapping("/job")
    public AjaxResult add(@RequestBody SysJob job) throws Exception;

    @DeleteMapping("/job/removeByMeetingId")
    public AjaxResult removeByMeetingId(@RequestParam("meetingId") Long meetingId) throws Exception;


    @PutMapping("/job/editByMeetingId")
    public AjaxResult editByMeetingId(
            @RequestParam("meetingId") Long meetingId,
            @RequestParam("time") String time,
            @RequestParam("type") String type) throws Exception;
}
