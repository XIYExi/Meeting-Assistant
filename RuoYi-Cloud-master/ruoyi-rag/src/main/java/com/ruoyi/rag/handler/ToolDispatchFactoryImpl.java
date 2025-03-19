package com.ruoyi.rag.handler;

import com.ruoyi.rag.config.ToolDispatchEnum;
import com.ruoyi.rag.config.ToolExecuteMap;
import com.ruoyi.rag.declare.ToolDispatchFactory;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.model.CustomPrompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ToolDispatchFactoryImpl implements ToolDispatchFactory, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ToolDispatchFactoryImpl.class);

    @Resource
    private ApplicationContext applicationContext;

    private static Map<String, ToolSimpleHandler> toolMap = new HashMap<>();

    @Override
    public String dispatch(String type, String content) {
        ToolSimpleHandler toolSimpleHandler = toolMap.get(type);
        String prompt = toolSimpleHandler.handler(content);
        return prompt;
    }

    @Override
    public String dispatch(List<StepSplitEntity> steps, String uid) {
        // 需要保存结果
        // 方便后面的结果调用
        Map<Integer, Map<String, Object>> stepResult = new HashMap<>();
        for (int i = 0; i < steps.size(); i++) {
            StepSplitEntity step = steps.get(i);
            stepResult.put(i, new HashMap<String, Object>());

            ToolSimpleHandler toolSimpleHandler = toolMap.get(step.getIntent());
            boolean handler = toolSimpleHandler.handler(step, i, stepResult, uid);
             // 准备开始下一轮之前，判断当前结果运行是不是失败
            if (!handler) {
                logger.info("【factory dispatch】解析错误，用户提问错误 ", step);
                break;
            }
        }

        // 把output结果拿出去进行判断 并输出最终的prompt
        String finalUserPrompt = this.handleOutput(stepResult);
        return finalUserPrompt;
    }


    private String handleOutput(Map<Integer, Map<String, Object>> output) {
        StringBuffer sb = new StringBuffer();
        sb.append("接下来你将得到一些信息，你需要以markdown格式返回，不要过度总结，不要重复总结，回答问题即可：");
        for (int i = 0; i < output.size(); i++) {
            Map<String, Object> currentStep = output.get(i);
            //判断当前结果，根据前面的逻辑，如果有执行错误，那么一定map的是最后一个元素
            // 直接返回最后一个元素的错误信息prompt 之前封装的sb都不需要
            boolean currentStatus = (boolean) currentStep.get("status");
            if (!currentStatus) {
                String failurePrompt = (String) currentStep.get("prompt");
                return failurePrompt;
            }
            // status都为true，那么正常执行，把数据封装到sb里面，最后返回sb.toString()的结果
            String currentIntent = (String) currentStep.get("intent");
            if ("route".equals(currentIntent)) {
                // 如果是route，检查下一个步骤（如果存在）
                if (i + 1 < output.size()) {
                    Map<String, Object> nextStep = output.get(i + 1);
                    String nextIntent = (String) nextStep.get("intent");
                    if ("action".equals(nextIntent)) {
                        // TODO route + action 直接跳转后执行
                    }
                }
                else {
                    // 否则直接执行execute函数封装对应的prompt
                    sb.append(this.executePrompt(currentIntent, currentStep));
                }
            }
            // 不是route类型，直接分发给execute函数封装prompt
            sb.append(this.executePrompt(currentIntent, currentStep));
        }
        return sb.toString();
    }

    private String executePrompt(String intent, Map<String, Object> currentOutput) {
        switch (intent) {
            case ToolExecuteMap.ROUTE:
                String formatRoute = "\n" + String.format(CustomPrompt.ROUTE_EXECUTE_PROMPT, currentOutput.get("routePath"), "前往") + "\n";
                return formatRoute;
            case ToolExecuteMap.QUERY:
                String formatQuery = "\n" + currentOutput.get("prompt") + "\n";
                return formatQuery;
            case ToolExecuteMap.ACTION:
                break;
            case ToolExecuteMap.SUMMARY:
                String formatSummary = "" + currentOutput.get("prompt");
                return formatSummary;
            case ToolExecuteMap.CHAT:
                String formatChat = "" + currentOutput.get("prompt");
                return formatChat;
        }
        return "";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        toolMap.put(ToolDispatchEnum.TOOL_ACTION.getMessage(), applicationContext.getBean(ToolActionHandler.class));
        toolMap.put(ToolDispatchEnum.TOOL_ROUTER.getMessage(), applicationContext.getBean(ToolRouteHandler.class));
        toolMap.put(ToolDispatchEnum.TOOL_CHAT.getMessage(), applicationContext.getBean(ToolChatHandler.class));
        toolMap.put(ToolDispatchEnum.TOOL_QUERY.getMessage(), applicationContext.getBean(ToolQueryHandler.class));
    }
}
