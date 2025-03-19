package com.ruoyi.rag.assistant.constant;

public enum QueryToolDispatchEnum {

    QUERY_MEETING(2001, "meeting"),
    QUERY_FILE(2002, "file"),
    QUERY_NEWS(2003, "news"),
    QUERY_RANk(2004, "rank"),
    QUERY_REC(2005, "rec");

    private Integer code;
    private String message;

    QueryToolDispatchEnum(int code, String message) {
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
