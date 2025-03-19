package com.ruoyi.rec.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rec.api.domain.MeetingResponse;
import com.ruoyi.rec.api.factory.RemoteRecFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteRecService", value = ServiceNameConstants.RAG_SERVICE, fallbackFactory = RemoteRecFallbackFactory.class)
public interface RemoteRecService {

    @GetMapping("/demo/rec_for_agent")
    public List<MeetingResponse> recForAgent(@RequestParam("userId") Long userId);
}
