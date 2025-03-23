package com.ruoyi.router.api.factory;

import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.router.api.RemoteRouterService;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RemoteRouterFallbackFactory implements FallbackFactory<RemoteRouterService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteRouterFallbackFactory.class);
    @Override
    public RemoteRouterService create(Throwable throwable) {
        log.error("im router服务调用失败:{}", throwable.getMessage());
        return new RemoteRouterService() {
            @Override
            public Mono<AjaxResult> sendMsg(Long userId, String msgJson) {
                return Mono.just(AjaxResult.error("Reactive OpenFeign Rpc Error: " + throwable.getMessage()));
            }

            @Override
            public AjaxResult batchSendMsg(List<ImMsgBody> imMsgBody) {
                return AjaxResult.error("im router send batch msgs error");
            }
        };
    }
}