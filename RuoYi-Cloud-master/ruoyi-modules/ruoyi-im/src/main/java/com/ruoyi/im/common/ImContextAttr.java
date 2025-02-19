package com.ruoyi.im.common;

import io.netty.util.AttributeKey;

public class ImContextAttr {
    // 绑定netty channelhandlercontext信息
    public static AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");

    public static AttributeKey<Integer> APP_ID = AttributeKey.valueOf("appId");


    public static AttributeKey<Integer> ROOM_ID = AttributeKey.valueOf("roomId");
}
