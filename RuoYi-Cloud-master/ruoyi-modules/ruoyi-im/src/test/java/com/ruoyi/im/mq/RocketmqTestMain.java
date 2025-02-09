package com.ruoyi.im.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

public class RocketmqTestMain {
    public static void main(String[] args) throws Exception {

         // 声明group
        DefaultMQProducer producer = new DefaultMQProducer("group_test");

        // 声明namesrv地址
        producer.setNamesrvAddr("1.13.154.224:9876");

        // 启动实例
        producer.start();

        // 设置消息的topic,tag以及消息体
        Message msg = new Message("topic_test", "tag_test", "消息内容".getBytes(StandardCharsets.UTF_8));

        // 发送消息，并设置10s连接超时
        SendResult send = producer.send(msg, 10000);
        System.out.println("发送结果："+send);




        // 关闭实例
        producer.shutdown();
    }
}
