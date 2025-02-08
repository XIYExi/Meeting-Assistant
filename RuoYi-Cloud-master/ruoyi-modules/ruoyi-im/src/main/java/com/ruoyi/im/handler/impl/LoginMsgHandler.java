package com.ruoyi.im.handler.impl;

import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录消息报处理逻辑
 */
public class LoginMsgHandler implements SimpleHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        logger.info("进入登录处理函数... {}", imMsg);
        ctx.writeAndFlush(imMsg);
    }
}
