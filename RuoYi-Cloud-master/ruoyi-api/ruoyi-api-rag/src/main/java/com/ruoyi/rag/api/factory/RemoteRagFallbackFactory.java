package com.ruoyi.rag.api.factory;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.api.RemoteRagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class RemoteRagFallbackFactory implements FallbackFactory<RemoteRagService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteRagService.class);
    @Override
    public RemoteRagService create(Throwable throwable) {
        log.error("RAG 服务调用失败:{}", throwable.getMessage());
        return new RemoteRagService() {
            @Override
            public AjaxResult insert(Long originalId, Long dbType, String title) {
                return AjaxResult.error("milvus向量库插入失败");
            }
        };
    }
}
