package com.ruoyi.router.rpc;

import com.ruoyi.router.service.ImRouterService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ImRouteRpcImpl implements ImRouterRpc{

    @Resource
    private ImRouterService routerService;


    @Override
    public boolean sendMsg(Long objectId, String msgJson) {
        return routerService.sendMsg(objectId, msgJson);
    }
}
