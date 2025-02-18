package com.ruoyi.im.handler;

import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImContextAttr;
import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImCoreServerConstants;
import com.ruoyi.im.handler.impl.ImHandlerFactoryImpl;
import com.ruoyi.im.handler.impl.LogoutMsgHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * IM消息统一handler入口
 */
@Component
@ChannelHandler.Sharable
public class ImServerCoreHandler extends SimpleChannelInboundHandler {

    @Resource
    private ImHandlerFactory imHandlerFactory;
    @Autowired
    private LogoutMsgHandler logoutMsgHandler;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (! (msg instanceof ImMsg)) {
            throw new IllegalAccessException("error msg, msg type must be ImMsg, current msg is : " + msg);
        }
        ImMsg imMsg = (ImMsg) msg;
        // factory用来分发不同code对应的处理函数
        imHandlerFactory.doMsgHandler(ctx, imMsg);
    }


    /**
     * 正常或者意外短线，都会触发此函数
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Long userId = ctx.attr(ImContextAttr.USER_ID).get();
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId != null && appId != null) {
            // logoutMsgHandler.logoutHandler(ctx,userId,appId);
            ChannelHandlerContextCache.remove(userId);
            redisTemplate.delete(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId);
        }
    }
}
