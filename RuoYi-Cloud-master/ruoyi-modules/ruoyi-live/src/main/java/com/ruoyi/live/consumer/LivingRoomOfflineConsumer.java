package com.ruoyi.live.consumer;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.entity.im.ImOfflineDTO;
import com.ruoyi.common.mq.properties.RocketMQConsumerProperties;
import com.ruoyi.common.mq.topic.ImCoreServerProviderTopicName;
import com.ruoyi.live.service.LiveRoomService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LivingRoomOfflineConsumer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LivingRoomOfflineConsumer.class);

    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private LiveRoomService liveRoomService;

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + LivingRoomOfflineConsumer.class.getSimpleName());
        // 一次从broker中拉取10条消息到本地内存中进行处理
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 监听im发送过来的业务消息topic
        mqPushConsumer.subscribe(ImCoreServerProviderTopicName.IM_OFFLINE_MSG_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                ImOfflineDTO imOfflineDTO = JSON.parseObject(new String(msg.getBody()), ImOfflineDTO.class);
                liveRoomService.userOfflineHandler(imOfflineDTO);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        logger.info("mq im-online 消费者启动成功， namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
