package com.ruoyi.im.handler.impl;


import com.alibaba.fastjson2.JSON;
import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.entity.ImMsgBody;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 登出消息处理逻辑
 */
@Component
public class LogoutMsgHandler implements SimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        logger.info("进入登出处理函数... {}", imMsg);
        // 理想情况下，客户端会发送完整的下线消息报
        Long userId = ImContextUtils.getUserId(ctx);
        if (userId == null) {
            logger.error("attr error, imMsg is {}", imMsg);
            throw new IllegalArgumentException("attr is error");
        }
        ChannelHandlerContextCache.remove(userId);
        ctx.close();
    }

    /**
     * 登出的时候，发送确认信号，这个是正常网络断开才会发送，异常断线则不发送
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    private void logoutMsgNotice(ChannelHandlerContext ctx, Long userId, Integer appId) {
        ImMsgBody respBody = new ImMsgBody();
        respBody.setAppId(appId);
        respBody.setUserId(userId);
        respBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGOUT_MSG.getCode(), JSON.toJSONString(respBody));
        ctx.writeAndFlush(respMsg);
        ctx.close();
    }
}
