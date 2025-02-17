package com.ruoyi.live.handler;

import com.ruoyi.live.entity.ImMsgBody;
import io.netty.channel.ChannelHandlerContext;

public interface ImMsgBizFactory {

    void doMsgHandler(ImMsgBody imMsgBody);
}
