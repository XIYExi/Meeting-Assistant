package com.ruoyi.rag.assistant.constant;

public enum ActionToolDispatchEnum {

    ACTION_CHECKIN(4001, "checkin"),
    ACTION_SUMMARY(4002, "summarize"),
    ACTION_CANCEL(4003, "cancel"),
    ACTION_SHARE(4004, "share"),
    ACTION_DOWNLOAD(4005, "download"),
    ACTION_SCHEDULE(4006, "schedule");

    private Integer code;
    private String message;

    ActionToolDispatchEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode(){
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}
