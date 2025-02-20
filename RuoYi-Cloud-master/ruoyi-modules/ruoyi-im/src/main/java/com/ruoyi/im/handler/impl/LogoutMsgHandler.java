package com.ruoyi.im.handler.impl;


import com.alibaba.fastjson.JSON;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.ImOfflineDTO;
import com.ruoyi.common.mq.topic.ImCoreServerProviderTopicName;
import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImCoreServerConstants;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登出消息处理逻辑
 */
@Component
public class LogoutMsgHandler implements SimpleHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutMsgHandler.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MQProducer mqProducer;


    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        logger.info("进入登出处理函数... {}", imMsg);
        // 理想情况下，客户端会发送完整的下线消息报
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null) {
            logger.error("attr error, imMsg is {}", imMsg);
            throw new IllegalArgumentException("attr is error");
        }
        logoutHandler(ctx, userId, appId);
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



    /**
     * 登出的时候做缓存的清理和mq通知
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    public void logoutHandler(ChannelHandlerContext ctx, Long userId, Integer appId) {
        logger.info("[LogoutMsgHandler] logout success,userId is {},appId is {}", userId, appId);
        //理想情况下，客户端断线的时候，会发送一个断线消息包
        ChannelHandlerContextCache.remove(userId);
        stringRedisTemplate.delete(ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId);
        ImContextUtils.removeUserId(ctx);
        ImContextUtils.removeAppId(ctx);
        sendLogoutMQ(ctx, userId, appId);
    }

    /**
     * 登出的时候发送mq消息
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    public void sendLogoutMQ(ChannelHandlerContext ctx, Long userId, Integer appId) {
        ImOfflineDTO imOfflineDTO = new ImOfflineDTO();
        imOfflineDTO.setUserId(userId);
        imOfflineDTO.setAppId(appId);
        imOfflineDTO.setRoomId(ImContextUtils.getRoomId(ctx));
        imOfflineDTO.setLoginTime(System.currentTimeMillis());

        Message message = new Message();
        message.setTopic(ImCoreServerProviderTopicName.IM_OFFLINE_MSG_TOPIC);
        message.setBody(JSON.toJSONString(imOfflineDTO).getBytes());
        try {
            SendResult send = mqProducer.send(message);
            logger.info("[sendLogoutMQ] send result {}", send);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
