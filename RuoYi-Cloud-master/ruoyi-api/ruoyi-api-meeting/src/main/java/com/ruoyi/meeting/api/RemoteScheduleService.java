package com.ruoyi.meeting.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.meeting.api.factory.RemoteScheduleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 这是调用Meeting的api，写定时任务的时候产生的历史遗留问题
 */
@FeignClient(contextId = "remoteScheduleService", value = ServiceNameConstants.MEETING_SERVICE, fallbackFactory = RemoteScheduleFallbackFactory.class)
public interface RemoteScheduleService {


    /**
     * 更新会议状态，给定时任务调用的
     * @param meetingId
     * @param status
     * @return
     */
    @GetMapping("/meeting/updateMeetingStatus")
    public AjaxResult updateMeetingStatus(@RequestParam("id") Long meetingId, @RequestParam("status") int status);

    /**
     * 根据dbType数据库类型获取对应的数据，给RAG调用的，比对之后获取匹配的数据内容
     * @param id
     * @param dbType
     * @return
     */
    @GetMapping("/meeting/getMessageByAgenda")
    public AjaxResult getMessageByAgenda(@RequestParam("id") Long id, @RequestParam("dbType") Long dbType);

}
