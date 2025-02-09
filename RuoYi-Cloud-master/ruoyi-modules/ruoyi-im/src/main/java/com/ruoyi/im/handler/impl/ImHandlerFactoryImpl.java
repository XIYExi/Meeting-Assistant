package com.ruoyi.im.handler.impl;

import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.handler.ImHandlerFactory;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息处理函数工厂实现类
 */
@Component
public class ImHandlerFactoryImpl implements ImHandlerFactory, InitializingBean {

    private static final Map<Integer, SimpleHandler> simpleHandlerMap = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;


    @Override
    public void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg) {
        SimpleHandler simpleHandler = simpleHandlerMap.get(imMsg.getCode());
        if (simpleHandler == null) {
            throw new IllegalArgumentException("msg code is error, code is : " + imMsg.getCode());
        }
        simpleHandler.handler(ctx, imMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        simpleHandlerMap.put(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), applicationContext.getBean(LoginMsgHandler.class));
        simpleHandlerMap.put(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), applicationContext.getBean(LogoutMsgHandler.class));
        simpleHandlerMap.put(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), applicationContext.getBean(BizMsgHandler.class));
        simpleHandlerMap.put(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), applicationContext.getBean(HeartBeatMsgHandler.class));
    }
}
