package com.ruoyi.live.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.live.api.factory.RemoteLiveFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteImService", value = ServiceNameConstants.LIVE_SERVICE, fallbackFactory = RemoteLiveFallbackFactory.class)
public interface RemoteLiveService {

    @PostMapping("/living/queryByRoomId")
    public AjaxResult queryByRoomId(@RequestParam("roomId") Long roomId, @RequestParam("appId") Integer appId);
}
