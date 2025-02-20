package com.ruoyi.common.mq.topic;

public class ImCoreServerProviderTopicName {

    /**
     * 接收im系统发送的业务信息
     */
    public static final String LIVE_IM_BIZ_MSG_TOPIC = "live_in_biz_msg_topic";


    /**
     * 发送im的ack消息
     */
    public static final String LIVE_IM_ACK_MSG_TOPIC = "live_im_ack_msg_topic";


    /**
     * im登录消息
     */
    public static final String IM_ONLINE_MSG_TOPIC = "im_online_topic";

    /**
     * im退出消息
     */
    public static final String IM_OFFLINE_MSG_TOPIC = "im_offline_topic";
}
