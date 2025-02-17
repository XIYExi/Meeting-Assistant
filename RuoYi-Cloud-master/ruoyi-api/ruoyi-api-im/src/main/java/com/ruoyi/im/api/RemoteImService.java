package com.ruoyi.im.api;


import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.im.api.factory.RemoteImFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * im远程调用服务
 */
@FeignClient(contextId = "remoteImService", value = ServiceNameConstants.IM_SERVICE, fallbackFactory = RemoteImFallbackFactory.class)
public interface RemoteImService {

    @GetMapping(value = "/im/rpc")
    public AjaxResult rpc(@RequestParam("userId") Long userId, @RequestParam("msgJson") String msgJson);

}
