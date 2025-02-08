package com.ruoyi.im.handler.impl;

import com.alibaba.fastjson.JSON;
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
 * 登录消息报处理逻辑
 */
public class LoginMsgHandler implements SimpleHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginMsgHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        byte[] body = imMsg.getBody();

        if (body == null || body.length == 0) {
            ctx.close();
            logger.error("body error, isMsg is {}", imMsg);
            throw new IllegalArgumentException("body error");
        }
        ImMsgBody imMsgBody = JSON.parseObject(new String(body), ImMsgBody.class);
        Long userId = SecurityUtils.getUserId();

        if (!Objects.equals(userId, imMsgBody.getUserId())) {
            ctx.close();
            logger.error("userid error, user-transparent is {}, however user-local is {}", imMsgBody.getUserId(), userId);
            throw new IllegalArgumentException("userid error");
        }

        // 根据userId保存channel对应信息
        ChannelHandlerContextCache.put(userId, ctx);
        // Todo


        ImMsgBody respBody = new ImMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
        ImMsg respMsg = ImMsg.build(ImMsgCodeeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(respBody));
        ctx.writeAndFlush(respMsg);
        ctx.close();
        logger.info("登录函数处理完毕... {}", respMsg);
    }
}
