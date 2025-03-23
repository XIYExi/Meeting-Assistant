package com.ruoyi.im.api;


import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.im.api.factory.RemoteImFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * im远程调用服务
 */
@ReactiveFeignClient(value = ServiceNameConstants.IM_SERVICE, fallbackFactory = RemoteImFallbackFactory.class)
public interface RemoteImService {

    @GetMapping(value = "/im/rpc")
    public Mono<AjaxResult> rpc(@RequestParam("msgJson") String msgJson);



//    @PostMapping("/im/batchRpc")
//    public AjaxResult batchRpc(@RequestBody List<ImMsgBody> imMsgBodyList);
}
