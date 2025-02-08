package com.ruoyi.im.entity;

import java.io.Serializable;

public class ImMsgBody implements Serializable {

    // im接入的服务id
    private int appId;

    // 用户id
    private long userId;

    // 和业务服务进行消息传递
    private String data;

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
}
