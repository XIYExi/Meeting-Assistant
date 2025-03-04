package com.ruoyi.rag.tcp.domain;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class TcpQueue {
    private ChannelHandlerContext channelHandlerContext;
    private String uId ;
    private String channelId;
}
