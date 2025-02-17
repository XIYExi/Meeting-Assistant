package com.ruoyi.im.rpc;

import org.springframework.stereotype.Component;

@Component
public class RouterHandleRpcImpl implements IRouterHandlerRpc{

    @Override
    public void sendMsg(Long userId, String msgJson) {
        System.out.println("this is im-core-server");
    }
}
