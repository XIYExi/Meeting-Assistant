package com.ruoyi.rag.config;

public enum ToolDispatchEnum {

    TOOL_ROUTER(1001, "route"),
    TOOL_ACTION(1002, "action"),
    TOOL_CHAT(1003, "chat"),
    TOOL_QUERY(1004, "query"),
    TOOL_SUMMARY(1005, "summary");

    private Integer code;
    private String message;

    ToolDispatchEnum(int code, String message) {
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
