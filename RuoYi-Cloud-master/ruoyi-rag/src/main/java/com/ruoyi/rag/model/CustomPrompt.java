package com.ruoyi.rag.model;

public interface CustomPrompt {

    public static final String ROUTE_FILTER_EMPTY_IN_LEAF = "步骤【%s】中，要求跳转目标页面【%s】，信息匹配为子页面，但是跳转参数匹配为空，请返回没有匹配到的错误信息，让用户提供更多的参数后重试。";

    public static final String ROUTE_OUTPUT_PROMPT = "要求跳转目标页面【%s】，跳转参数为【%s】，拼接后跳转路径为【%s】";

    public static final String ROUTE_MAIN_OUTPUT_PROMPT = "要求跳转目标页面【%s】，跳转路径为【%s】";

    public static final String ROUTE_EXECUTE_PROMPT = "\t请给我一段描述，最后以: <view style=\"text-decoration: underline;font-size: 14px;color: #333;margin: 10px;\" onclick=\"handleRoute('%s')\">\n" +
            "\t\t\t\t\t%s\n" +
            "\t\t\t\t</view>结尾.";

    public static final String QUERY_MEETING_SUCCESS_PROMPT = "查询到和会议有关信息，会议标题【%s】，开始时间【%s】，地点【%s】，浏览量、关注度【%s】，会议内容【%s】, 会议海报【%s】";

    public static final String QUERY_MEETING_AGENDA_SUCCESS_PROMPT = "查询到对应议程信息，议程列表【%s】,其中meta字段表示该议程出席嘉宾和职务，总结时需要体现";

    public static final String QUERY_GEO_SUCCESS_PROMPT = "查询到对应位置信息，会议地点【%s】，更多信息【%s】，经纬度【%s】";

    public static final String QUERY_MEETING_FAILURE_PROMPT = "数据库中无法检索到当前会议信息，请确认会议信息后重试";

    public static final String QUERY_EXECUTE_PROMPT = "\t当前步骤为查询操作，下列信息是数据库查询结果，请根据传递信息总结内容，并回答用户相关问题,请使用markdown或者html格式回答，排版好看，注意换行：【%s】";
}