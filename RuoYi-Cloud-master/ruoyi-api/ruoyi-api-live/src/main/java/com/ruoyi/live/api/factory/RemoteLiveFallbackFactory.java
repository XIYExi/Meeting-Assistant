package com.ruoyi.live.api.factory;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.live.api.RemoteLiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteLiveFallbackFactory  implements FallbackFactory<RemoteLiveService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteLiveFallbackFactory.class);
    @Override
    public RemoteLiveService create(Throwable throwable) {
        log.error("会议服务调用失败:{}", throwable.getMessage());
        return new RemoteLiveService() {
            @Override
            public AjaxResult queryByRoomId(Long roomId, Integer appId) {
                return AjaxResult.error("queryByRoomId 远程调用错误");
            }
        };
    }
}