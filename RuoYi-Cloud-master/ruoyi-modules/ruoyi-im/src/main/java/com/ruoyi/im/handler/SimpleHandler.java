package com.ruoyi.im.handler;

import com.ruoyi.im.common.ImMsg;
import io.netty.channel.ChannelHandlerContext;


public interface SimpleHandler {

    /**
     * 消息处理函数
     * @param ctx
     * @param imMsg
     */
    void handler(ChannelHandlerContext ctx, ImMsg imMsg);

}
