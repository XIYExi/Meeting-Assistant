package com.ruoyi.im.constant;

public enum AppIdEnum {

     // 登录消息包，将channel 和 userId 关联
    LIVE_BIZ(10001, "线上会议直播业务"),
    AGENT_BIZ(10002, "Agent智能体对话业务");

    private int code;
    private String desc;

    AppIdEnum(int code, String desc) {
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
