package com.ruoyi.im.api.factory;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.im.api.RemoteImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteImFallbackFactory  implements FallbackFactory<RemoteImService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteImFallbackFactory.class);
    @Override
    public RemoteImService create(Throwable throwable) {
        log.error("会议服务调用失败:{}", throwable.getMessage());
        return new RemoteImService() {
            @Override
            public AjaxResult rpc(Long userId, String msgJson) {
                return AjaxResult.error("im rpc调用失败");
            }
        };
    }
}
