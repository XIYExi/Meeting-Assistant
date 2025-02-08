package com.ruoyi.im.handler;

import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.handler.impl.ImHandlerFactoryImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ImServerCoreHandler extends SimpleChannelInboundHandler {

    private ImHandlerFactory imHandlerFactory = new ImHandlerFactoryImpl();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (! (msg instanceof ImMsg)) {
            throw new IllegalAccessException("error msg, msg type must be ImMsg, current msg is : " + msg);
        }
        ImMsg imMsg = (ImMsg) msg;
        // factory用来分发不同code对应的处理函数
        imHandlerFactory.doMsgHandler(ctx, imMsg);
    }
}
