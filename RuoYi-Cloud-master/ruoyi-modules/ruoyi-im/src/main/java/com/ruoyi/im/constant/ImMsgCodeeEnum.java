package com.ruoyi.im.constant;

public enum ImMsgCodeeEnum {

    // 登录消息包，将channel 和 userId 关联
    IM_LOGIN_MSG(1001, "登录消息包"),
    // 登出消息包，正常断开im时发送的
    IM_LOGOUT_MSG(1001, "登出消息包"),
    // 业务消息包，常用的消息类型
    IM_BIZ_MSG(1001, "业务消息包"),
    // 心跳消息包

    IM_HEARTBEAT_MSG(1001, "心跳消息包");
    private int code;
    private String desc;

    ImMsgCodeeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
