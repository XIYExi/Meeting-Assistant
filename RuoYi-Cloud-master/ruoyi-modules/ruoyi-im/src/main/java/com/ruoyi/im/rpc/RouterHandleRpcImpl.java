package com.ruoyi.im.rpc;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.im.entity.ImMsgBody;
import com.ruoyi.im.service.IRouterHandlerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RouterHandleRpcImpl implements IRouterHandlerRpc{

    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void sendMsg(Long userId, String msgJson) {
        System.out.println("this is im-core-server");
        ImMsgBody imMsgBody = JSON.parseObject(msgJson, ImMsgBody.class);
        routerHandlerService.onReceive(imMsgBody);
    }
}
