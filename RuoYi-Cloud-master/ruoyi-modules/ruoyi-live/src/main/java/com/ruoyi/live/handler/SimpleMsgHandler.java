package com.ruoyi.live.handler;

import com.ruoyi.live.entity.ImMsgBody;
import io.netty.channel.ChannelHandlerContext;

public interface SimpleMsgHandler {
    void handler(ImMsgBody imMsgBody);
}
