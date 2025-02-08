package com.ruoyi.im.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ChannelHandlerContextCache {

    private static Map<Long, ChannelHandlerContext> channelHandlerContextMap = new HashMap<>();

    public static ChannelHandlerContext get(Long userId) {
        return channelHandlerContextMap.get(userId);
    }

    public static void put(Long userId, ChannelHandlerContext ctx) {
        channelHandlerContextMap.put(userId, ctx);
    }

    public static void remove(Long userId) {
        channelHandlerContextMap.remove(userId);
    }

}
