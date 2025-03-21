package com.ruoyi.im.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ChannelHandlerContextCache {

    /**
     * 当前im服务器启动时候，对外暴露的ip和端口
     */
    private static String SERVER_IP_ADDRESS = "";

    public static String getServerIpAddress() {
        return SERVER_IP_ADDRESS;
    }

    public static void setServerIpAddress(String serverIpAddress) {
        SERVER_IP_ADDRESS = serverIpAddress;
    }

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
