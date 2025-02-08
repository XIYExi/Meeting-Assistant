package com.ruoyi.im.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.AppIdEnum;
import com.ruoyi.im.constant.ImMsgCodeeEnum;
import com.ruoyi.im.entity.ImMsgBody;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 登出消息处理逻辑
 */
public class LogoutMsgHandler implements SimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        logger.info("进入登出处理函数... {}", imMsg);

        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            logger.error("body error, body is {}", imMsg);
            throw new IllegalArgumentException("body error");
        }

        // 理想情况下，客户端会发送完整的下线消息报
        ImMsgBody imMsgBody = JSON.parseObject(new String(body), ImMsgBody.class);
        ChannelHandlerContextCache.remove(imMsgBody.getUserId());

    }
}
