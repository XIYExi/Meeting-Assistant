package com.ruoyi.rag.model;

public interface CustomPrompt {

    public static final String ROUTE_FILTER_EMPTY_IN_LEAF = "步骤【%s】中，要求跳转目标页面【%s】，信息匹配为子页面，但是跳转参数匹配为空，请返回没有匹配到的错误信息，让用户提供更多的参数后重试。";


    public static final String ROUTE_OUTPUT_PROMPT = "要求跳转目标页面【%s】，跳转参数为【%s】，拼接后跳转路径为【%s】";

    public static final String ROUTE_MAIN_OUTPUT_PROMPT = "要求跳转目标页面【%s】，跳转路径为【%s】";

    public static final String ROUTE_EXECUTE_PROMPT = "\t请给我一段描述，最后以: <view style=\"text-decoration: underline;font-size: 14px;color: #333;margin: 10px;\" onclick=\"handleRoute('%s')\">\n" +
            "\t\t\t\t\t%s\n" +
            "\t\t\t\t</view>结尾.";


}