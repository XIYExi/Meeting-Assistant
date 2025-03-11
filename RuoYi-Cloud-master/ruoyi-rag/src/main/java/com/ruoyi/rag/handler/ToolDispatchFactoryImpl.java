package com.ruoyi.rag.handler;

import com.ruoyi.rag.config.ToolDispatchEnum;
import com.ruoyi.rag.declare.ToolDispatchFactory;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.domain.StepSplitParamsEntity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ToolDispatchFactoryImpl implements ToolDispatchFactory, InitializingBean {

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
        for (StepSplitEntity step : steps) {
            String intent = step.getIntent();
            step.getParams()
            ToolSimpleHandler toolSimpleHandler = toolMap.get(intent);
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
