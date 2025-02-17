package com.ruoyi.live.enums;

public enum ImMsgBizCodeEnum {

    LIVE_ROOM_IM_BIZ(5555, "直播间聊天im西澳西");

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
