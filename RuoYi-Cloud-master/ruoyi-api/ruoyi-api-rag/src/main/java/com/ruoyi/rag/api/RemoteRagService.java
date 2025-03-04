package com.ruoyi.rag.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.api.factory.RemoteRagFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "remoteRagService", value = ServiceNameConstants.RAG_SERVICE, fallbackFactory = RemoteRagFallbackFactory.class)
public interface RemoteRagService {

    @GetMapping("/milvus/insert")
    public AjaxResult insert(@RequestParam("id") Long originalId, @RequestParam("dbType") Long dbType, @RequestParam("title") String title);
}
