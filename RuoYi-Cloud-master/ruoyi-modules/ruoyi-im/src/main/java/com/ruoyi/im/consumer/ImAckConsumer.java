package com.ruoyi.im.consumer;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.mq.properties.RocketMQConsumerProperties;
import com.ruoyi.common.mq.topic.ImCoreServerProviderTopicName;
import com.ruoyi.im.service.IMsgAckCheckService;
import com.ruoyi.im.service.IRouterHandlerService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ImAckConsumer implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImAckConsumer.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private IMsgAckCheckService msgAckCheckService;
    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        //设置我们的namesrv地址
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        //声明消费组
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ImAckConsumer.class.getSimpleName());
        //每次只拉取一条消息
        mqPushConsumer.setConsumeMessageBatchMaxSize(1);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        mqPushConsumer.subscribe(ImCoreServerProviderTopicName.LIVE_IM_ACK_MSG_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            String json = new String(msgs.get(0).getBody());
            ImMsgBody imMsgBody = JSON.parseObject(json, ImMsgBody.class);
            int retryTimes = msgAckCheckService.getMsgAckTimes(imMsgBody.getMsgId(), imMsgBody.getUserId(), imMsgBody.getAppId());
            LOGGER.info("retryTimes is {},msgId is {}", retryTimes, imMsgBody.getMsgId());
            if (retryTimes < 0) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            //只支持一次重发
            if (retryTimes < 2) {
                msgAckCheckService.recordMsgAck(imMsgBody, retryTimes + 1);
                msgAckCheckService.sendDelayMsg(imMsgBody);
                // 这里才是真正的消息发送入口！
                routerHandlerService.sendMsgToClient(imMsgBody);
            } else {
                msgAckCheckService.doMsgAck(imMsgBody);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("mq消费者启动成功,namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
