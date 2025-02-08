package com.ruoyi.im.handler.impl;

import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登出消息处理逻辑
 */
public class LogoutMsgHandler implements SimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutMsgHandler.class);
    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        logger.info("进入登出处理函数... {}", imMsg);
        ctx.writeAndFlush(imMsg);
    }
}
