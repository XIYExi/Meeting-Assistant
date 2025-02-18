package com.ruoyi.im.rpc;

import com.ruoyi.im.service.ImOnlineService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ImOnlineRpcImpl implements ImOnlineRpc{

    @Resource
    private ImOnlineService imOnlineService;
    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineService.isOnline(userId, appId);
    }
}
