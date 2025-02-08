package com.ruoyi.im.handler;

import com.ruoyi.im.common.ImMsg;
import io.netty.channel.ChannelHandlerContext;

public interface ImHandlerFactory {

    void doMsgHandler(ChannelHandlerContext ctx, ImMsg imMsg);

}
