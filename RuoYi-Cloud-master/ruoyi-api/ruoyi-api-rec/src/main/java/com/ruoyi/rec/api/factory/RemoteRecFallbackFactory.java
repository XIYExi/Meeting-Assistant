package com.ruoyi.rec.api.factory;

import com.ruoyi.rec.api.RemoteRecService;
import com.ruoyi.rec.api.domain.MeetingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RemoteRecFallbackFactory implements FallbackFactory<RemoteRecService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteRecService.class);
    @Override
    public RemoteRecService create(Throwable throwable) {
        log.error("RAG 服务调用失败:{}", throwable.getMessage());
        return new RemoteRecService() {
            @Override
            public List<MeetingResponse> recForAgent(Long userId) {
                return List.of();
            }
        };
    }
}
