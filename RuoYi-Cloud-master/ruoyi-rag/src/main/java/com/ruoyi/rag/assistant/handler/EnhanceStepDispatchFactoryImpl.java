package com.ruoyi.rag.assistant.handler;

import com.ruoyi.rag.assistant.constant.EnhancedToolDispatchEnum;
import com.ruoyi.rag.assistant.declare.EnhancedStepDispatchFactory;
import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.config.ToolDispatchEnum;
import com.ruoyi.rag.declare.ToolSimpleHandler;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.handler.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Component
public class EnhanceStepDispatchFactoryImpl implements EnhancedStepDispatchFactory/*, InitializingBean */{

    private static final Logger logger = LoggerFactory.getLogger(ToolDispatchFactoryImpl.class);
    private static Map<String, EnhancedToolHandler> toolMap = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;



    @Override
    public String dispatch(List<StepDefinition> steps, String uid) {
        QueryContext context = new QueryContext();
        steps.sort(Comparator.comparingInt(StepDefinition::getStep));
        for (StepDefinition step : steps) {
//            validateDependency(step, context);
            EnhancedToolHandler enhancedToolHandler = toolMap.get(step.getIntent());
            boolean handler = enhancedToolHandler.handler(step, context);
        }
        return null;
    }

    private void validateDependency(StepDefinition step, QueryContext context) {
        if (step.getDependency() != -1) {
            String dependencyKey = "step" + step.getDependency();
            if (!context.containsStep(dependencyKey)) {
                throw new IllegalStateException("Missing dependency for step " + step.getStep());
            }
        }
    }


//    @Override
//    public void afterPropertiesSet() throws Exception {
//        toolMap.put(EnhancedToolDispatchEnum.TOOL_ACTION.getMessage(), applicationContext.getBean(EnhancedActionToolHandler.class));
//        toolMap.put(EnhancedToolDispatchEnum.TOOL_ROUTER.getMessage(), applicationContext.getBean(EnhancedRouteToolHandler.class));
//        toolMap.put(EnhancedToolDispatchEnum.TOOL_CHAT.getMessage(), applicationContext.getBean(EnhancedChatToolHandler.class));
//        toolMap.put(EnhancedToolDispatchEnum.TOOL_QUERY.getMessage(), applicationContext.getBean(EnhancedQueryToolHandler.class));
//    }
    static {
        toolMap.put(EnhancedToolDispatchEnum.TOOL_ACTION.getMessage(), new EnhancedActionToolHandler());
        toolMap.put(EnhancedToolDispatchEnum.TOOL_ROUTER.getMessage(), new EnhancedRouteToolHandler());
        toolMap.put(EnhancedToolDispatchEnum.TOOL_CHAT.getMessage(), new EnhancedChatToolHandler());
        toolMap.put(EnhancedToolDispatchEnum.TOOL_QUERY.getMessage(), new EnhancedQueryToolHandler());
    }
}
