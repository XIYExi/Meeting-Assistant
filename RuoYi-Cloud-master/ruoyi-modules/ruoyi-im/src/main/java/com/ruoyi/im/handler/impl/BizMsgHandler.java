package com.ruoyi.im.handler.impl;

import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.common.mq.topic.ImCoreServerProviderTopicName;
import com.ruoyi.im.handler.SimpleHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BizMsgHandler implements SimpleHandler {

    private static final Logger logger = LoggerFactory.getLogger(BizMsgHandler.class);

    @Resource
    private MQProducer mqProducer;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {

        Long userId = ImContextUtils.getUserId(ctx);
        Integer appId = ImContextUtils.getAppId(ctx);
        if (userId == null || appId == null) {
            logger.error("attr error,imMsg is {}", imMsg);
            //有可能是错误的消息包导致，直接放弃连接
            ctx.close();
            throw new IllegalArgumentException("attr is error");
        }
        byte[] body = imMsg.getBody();
        if (body == null || body.length == 0) {
            logger.error("body error,imMsg is {}", imMsg);
            return;
        }

        Message message = new Message();
        message.setBody(body);
        message.setTopic(ImCoreServerProviderTopicName.LIVE_IM_BIZ_MSG_TOPIC);
        try {
            SendResult sendResult = mqProducer.send(message);
            logger.info("[BizMsgHandler消息投送结果]: {}", sendResult);
        } catch (Exception e) {
            logger.error("send error ,error is :", e);
            throw new RuntimeException(e);
        }
        // ctx.writeAndFlush(imMsg);
    }
}
