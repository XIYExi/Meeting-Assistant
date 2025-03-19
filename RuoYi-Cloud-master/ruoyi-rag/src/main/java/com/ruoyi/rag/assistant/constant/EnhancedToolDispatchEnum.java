package com.ruoyi.rag.assistant.constant;

public enum EnhancedToolDispatchEnum {

    TOOL_ROUTER(1001, "route"),
    TOOL_ACTION(1002, "action"),
    TOOL_CHAT(1003, "chat"),
    TOOL_QUERY(1004, "query");

    private Integer code;
    private String message;

    EnhancedToolDispatchEnum(int code, String message) {
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
