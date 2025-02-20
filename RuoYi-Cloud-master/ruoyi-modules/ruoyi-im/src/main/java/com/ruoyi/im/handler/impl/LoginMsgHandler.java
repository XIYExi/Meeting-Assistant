package com.ruoyi.im.handler.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.ImOnlineDTO;
import com.ruoyi.common.mq.topic.ImCoreServerProviderTopicName;
import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImConstants;
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
import java.util.concurrent.TimeUnit;


/**
 * 登录消息报处理逻辑
 */
@Component
public class LoginMsgHandler implements SimpleHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginMsgHandler.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MQProducer mqProducer;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        // 防止重复提交
        if (ImContextUtils.getUserId(ctx) != null) {
            return;
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            ctx.close();
            logger.error("body error, isMsg is {}", imMsg);
            throw new IllegalArgumentException("body error");
        }
        ImMsgBody imMsgBody = JSON.parseObject(new String(body), ImMsgBody.class);
        Long userId = imMsgBody.getUserId();
        Integer appId = imMsgBody.getAppId();
        if (appId < 10000) {
            ctx.close();
            logger.error("appid error,  current appid is {}",appId);
            throw new IllegalArgumentException("appId error");
        }
        if (userId == null || appId == null) {
            ctx.close();
            logger.error("userid error, user-transparent is {}, however user-local is {}", imMsgBody.getUserId(), userId);
            throw new IllegalArgumentException("userid error");
        }
        logger.info("current userid is {}", userId);

        // 根据userId保存channel对应信息
        ChannelHandlerContextCache.put(userId, ctx);
        // ctx.attr(ImContextAttr.USER_ID).set(userId);
        ImContextUtils.setUserId(ctx, userId);
        ImContextUtils.setAppId(ctx, appId);

        ImMsgBody respBody = new ImMsgBody();
        respBody.setUserId(userId);
        respBody.setAppId(appId);
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(respBody));

        stringRedisTemplate.opsForValue().set(
                ImCoreServerConstants.IM_BIND_IP_KEY + appId + ":" + userId ,
                ChannelHandlerContextCache.getServerIpAddress() + "%" + userId,
                ImConstants.DEFAULT_HEART_BEAT_GAP * 2,
                TimeUnit.SECONDS
        );
        logger.info("[LoginMsgHandler] login success,userId is {},appId is {}", userId, appId);
        ctx.writeAndFlush(respMsg);
        logger.info("登录函数处理完毕... {}", respMsg);
    }



    /**
     * 如果用户登录成功则处理相关记录
     *
     * @param ctx
     * @param userId
     * @param appId
     */
    public void loginSuccessHandler(ChannelHandlerContext ctx, Long userId, Integer appId, Integer roomId) {
        //按照userId保存好相关的channel对象信息
        ChannelHandlerContextCache.put(userId, ctx);
        ImContextUtils.setUserId(ctx, userId);
        ImContextUtils.setAppId(ctx, appId);
        if (roomId != null) {
            ImContextUtils.setRoomId(ctx, roomId);
        }
        //将im消息回写给客户端
        ImMsgBody respBody = new ImMsgBody();
        respBody.setAppId(appId);
        respBody.setUserId(userId);
        respBody.setData("true");
        ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(respBody));
        stringRedisTemplate.opsForValue()
                .set(ImCoreServerConstants.IM_BIND_IP_KEY +
                                appId + ":" + userId,

                        ChannelHandlerContextCache.getServerIpAddress() + "%" + userId,
                        ImConstants.DEFAULT_HEART_BEAT_GAP * 2, TimeUnit.SECONDS);
        logger.info("[LoginMsgHandler] login success,userId is {},appId is {}", userId, appId);
        ctx.writeAndFlush(respMsg);
        sendLoginMQ(userId, appId, roomId);
    }



    private void sendLoginMQ(Long userId, Integer appId, Integer roomId) {
        ImOnlineDTO imOnlineDTO = new ImOnlineDTO();
        imOnlineDTO.setUserId(userId);
        imOnlineDTO.setAppId(appId);
        imOnlineDTO.setRoomId(roomId);
        imOnlineDTO.setLoginTime(System.currentTimeMillis());
        Message message = new Message();
        message.setTopic(ImCoreServerProviderTopicName.IM_ONLINE_MSG_TOPIC);
        message.setBody(JSON.toJSONString(imOnlineDTO).getBytes());
        try {
            SendResult send = mqProducer.send(message);
            logger.info("[sendLoginMQ] sendResult: {}", send);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
