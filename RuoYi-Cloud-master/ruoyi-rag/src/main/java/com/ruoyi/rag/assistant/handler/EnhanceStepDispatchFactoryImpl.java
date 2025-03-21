package com.ruoyi.rag.assistant.handler;

import com.ruoyi.rag.assistant.constant.EnhancedToolDispatchEnum;
import com.ruoyi.rag.assistant.declare.EnhancedStepDispatchFactory;
import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
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
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EnhanceStepDispatchFactoryImpl implements EnhancedStepDispatchFactory, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EnhanceStepDispatchFactoryImpl.class);
    private static Map<String, EnhancedToolHandler> toolMap = new ConcurrentHashMap<>();

    @Resource
    private ApplicationContext applicationContext;


    @Override
    public String dispatch(List<StepDefinition> steps, String uid) {
        QueryContext context = new QueryContext();
        Map<String, Object> resultMap = new HashMap<>();
        steps.sort(Comparator.comparingInt(StepDefinition::getStep));

        for (StepDefinition step : steps) {

            int stepNum = step.getStep();
            String stepKey = "step" + stepNum;

            // 检查依赖
            int dependency = step.getDependency();
            if (dependency > 0 && !context.containsStep("step" + dependency)) {
                resultMap.put("error", "依赖步骤" + dependency + "的结果不存在");
                break;
            }

            EnhancedToolHandler enhancedToolHandler = toolMap.get(step.getIntent());
            boolean handler = enhancedToolHandler.handler(step, context);
        }

        Object dependencyResult = context.getDependencyResult(steps.size(), Object.class);
        System.err.println(dependencyResult);
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


    @Override
    public void afterPropertiesSet() throws Exception {
        toolMap.put(EnhancedToolDispatchEnum.TOOL_ACTION.getMessage(), applicationContext.getBean(EnhancedActionToolHandler.class));
        toolMap.put(EnhancedToolDispatchEnum.TOOL_ROUTER.getMessage(), applicationContext.getBean(EnhancedRouteToolHandler.class));
        toolMap.put(EnhancedToolDispatchEnum.TOOL_CHAT.getMessage(), applicationContext.getBean(EnhancedChatToolHandler.class));
        toolMap.put(EnhancedToolDispatchEnum.TOOL_QUERY.getMessage(), applicationContext.getBean(EnhancedQueryToolHandler.class));
    }

}
