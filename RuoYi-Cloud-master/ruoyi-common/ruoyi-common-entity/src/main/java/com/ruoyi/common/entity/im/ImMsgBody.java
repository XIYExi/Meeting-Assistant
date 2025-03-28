package com.ruoyi.common.entity.im;

import java.io.Serializable;

public class ImMsgBody implements Serializable {

    // im接入的服务id
    private int appId;

    // 用户id
    private long userId;

    // 业务标识
    private int bizCode;

    // 和业务服务进行消息传递
    private String data;

    //唯一的消息id
    private String msgId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getBizCode() {
        return bizCode;
    }

    public void setBizCode(int bizCode) {
        this.bizCode = bizCode;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "ImMsgBody{" +
                "appId=" + appId +
                ", userId=" + userId +
                ", bizCode=" + bizCode +
                ", data='" + data + '\'' +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
