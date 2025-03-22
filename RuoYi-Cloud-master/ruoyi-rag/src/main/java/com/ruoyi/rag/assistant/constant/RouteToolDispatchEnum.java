package com.ruoyi.rag.assistant.constant;

public enum RouteToolDispatchEnum {

    ROUTE_PAGE(3001, "page"),
    ROUTE_GEO(3002, "geo");

    private Integer code;
    private String message;

    RouteToolDispatchEnum(int code, String message) {
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
