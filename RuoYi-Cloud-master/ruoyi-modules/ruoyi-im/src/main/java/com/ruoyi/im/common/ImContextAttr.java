package com.ruoyi.im.common;

import io.netty.util.AttributeKey;

public class ImContextAttr {
    // 绑定netty channelhandlercontext信息
    public static AttributeKey<Long> USER_ID = AttributeKey.valueOf("userId");


}
