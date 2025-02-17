package com.ruoyi.router.service;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.im.api.RemoteImService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ImRouterServiceImpl implements ImRouterService {

    private Logger logger = LoggerFactory.getLogger(ImRouterServiceImpl.class);

    @Resource
    private RemoteImService remoteImService;

    @Override
    public boolean sendMsg(Long objectId, String msgJson) {
        AjaxResult rpc = remoteImService.rpc(objectId, msgJson);
        logger.info("rpc result {}", rpc);
        return false;
    }
}
