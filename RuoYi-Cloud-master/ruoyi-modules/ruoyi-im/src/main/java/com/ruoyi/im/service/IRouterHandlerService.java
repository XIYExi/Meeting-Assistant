package com.ruoyi.im.service;

import com.ruoyi.common.entity.im.ImMsgBody;

public interface IRouterHandlerService {

    /**
     * 当收到业务服务的请求，进行处理
     *
     * @param imMsgBody
     */
    void onReceive(ImMsgBody imMsgBody);


    /**
     * 发送消息给客户端
     *
     * @param imMsgBody
     */
    boolean sendMsgToClient(ImMsgBody imMsgBody);
}
