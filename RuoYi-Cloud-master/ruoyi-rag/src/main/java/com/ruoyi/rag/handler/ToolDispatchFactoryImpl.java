package com.ruoyi.rag.handler;

import com.ruoyi.rag.config.ToolDispatchEnum;
import com.ruoyi.rag.config.ToolExecuteMap;
import com.ruoyi.rag.declare.ToolDispatchFactory;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
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
    public String dispatch(List<StepSplitEntity> steps) {
        // 需要保存结果
        // 方便后面的结果调用
        Map<Integer, Map<String, Object>> stepResult = new HashMap<>();
        for (int i = 0; i < steps.size(); i++) {
            StepSplitEntity step = steps.get(i);
            stepResult.put(i, new HashMap<String, Object>());

            ToolSimpleHandler toolSimpleHandler = toolMap.get(step.getIntent());
            boolean handler = toolSimpleHandler.handler(step.getParams(), i, stepResult);
             // 准备开始下一轮之前，判断当前结果运行是不是失败
            if (!handler) {
                logger.info("【factory dispatch】解析错误，用户提问错误 ", step);
                break;
            }
        }

        // 把output结果拿出去进行判断
        this.handleOutput(stepResult);
        return "";
    }


    private void handleOutput(Map<Integer, Map<String, Object>> output) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < output.size(); i++) {
            Map<String, Object> currentStep = output.get(i);
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
    }

    private String executePrompt(String intent, Map<String, Object> currentOutput) {
        switch (intent) {
            case ToolExecuteMap.ROUTE:

                break;
            case ToolExecuteMap.QUERY:
                break;
            case ToolExecuteMap.ACTION:
                break;
            case ToolExecuteMap.SUMMARY:
                break;
        }
        return "";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        toolMap.put(ToolDispatchEnum.TOOL_ACTION.getMessage(), applicationContext.getBean(ToolActionHandler.class));
        toolMap.put(ToolDispatchEnum.TOOL_ROUTER.getMessage(), applicationContext.getBean(ToolRouteHandler.class));
        toolMap.put(ToolDispatchEnum.TOOL_CHAT.getMessage(), applicationContext.getBean(ToolChatHandler.class));
    }
}
