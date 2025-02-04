package com.ruoyi.meeting.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.factory.RemoteScheduleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteScheduleService", value = ServiceNameConstants.MEETING_SERVICE, fallbackFactory = RemoteScheduleFallbackFactory.class)
public interface RemoteScheduleService {


    @GetMapping("/meeting/updateMeetingStatus")
    public AjaxResult updateMeetingStatus(@RequestParam("id") Long meetingId, @RequestParam("status") int status);


}
