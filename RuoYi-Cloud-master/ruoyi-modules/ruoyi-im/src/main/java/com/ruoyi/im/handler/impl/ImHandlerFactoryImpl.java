package com.ruoyi.im.handler.impl;

import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImMsgCodeeEnum;
import com.ruoyi.im.handler.ImHandlerFactory;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理函数工厂实现类
 */
public class ImHandlerFactoryImpl implements ImHandlerFactory {

    private static Map<Integer, SimpleHandler> simpleHandlerMap = new HashMap<>();

    static {
        simpleHandlerMap.put(ImMsgCodeeEnum.IM_LOGIN_MSG.getCode(), new LoginMsgHandler());
        simpleHandlerMap.put(ImMsgCodeeEnum.IM_LOGOUT_MSG.getCode(), new LogoutMsgHandler());
        simpleHandlerMap.put(ImMsgCodeeEnum.IM_BIZ_MSG.getCode(), new BizMsgHandler());
        simpleHandlerMap.put(ImMsgCodeeEnum.IM_HEARTBEAT_MSG.getCode(), new HeartBeatMsgHandler());
    }

    @Override
    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg) {
        SimpleHandler simpleHandler = simpleHandlerMap.get(imMsg.getCode());
        if (simpleHandler == null) {
            throw new IllegalArgumentException("msg code is error, code is : " + imMsg.getCode());
        }
        simpleHandler.handler(ctx, imMsg);
    }
}
