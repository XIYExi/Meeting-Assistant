package com.ruoyi.im.rpc;


/**
 * 专门给router层的服务进行调用的接口
 */
public interface IRouterHandlerRpc {

    /**
     * 根据用户id进行消息的发送
     *
     * @param msgJson
     */
    void sendMsg(String msgJson);

}
