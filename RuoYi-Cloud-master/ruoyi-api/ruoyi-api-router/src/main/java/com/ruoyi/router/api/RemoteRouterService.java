package com.ruoyi.router.api;


import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.router.api.factory.RemoteRouterFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * im远程调用服务
 */
@ReactiveFeignClient(name = ServiceNameConstants.ROUTER_SERVICE, fallbackFactory = RemoteRouterFallbackFactory.class)
public interface RemoteRouterService {

    @GetMapping(value = "/router/sendMsg")
    public Mono<AjaxResult> sendMsg(@RequestParam("userId") Long userId, @RequestParam("msgJson") String msgJson);

    @GetMapping(value = "/router/batchSendMsg")
    public AjaxResult batchSendMsg(@RequestBody List<ImMsgBody> imMsgBody);

}
