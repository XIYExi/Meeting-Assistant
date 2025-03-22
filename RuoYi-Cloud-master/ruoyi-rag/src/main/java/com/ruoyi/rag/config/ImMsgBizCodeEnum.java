package com.ruoyi.rag.config;

public enum ImMsgBizCodeEnum {

    Agent_Chat_Code(5554, "Agent对话消息标识");

    int code;
    String desc;

    ImMsgBizCodeEnum(int code, String desc) {
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
