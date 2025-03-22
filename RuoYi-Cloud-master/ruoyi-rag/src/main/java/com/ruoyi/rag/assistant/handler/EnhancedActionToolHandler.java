package com.ruoyi.rag.assistant.handler;

import com.ruoyi.rag.assistant.constant.ActionToolDispatchEnum;
import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.handler.action.*;
import com.ruoyi.rag.assistant.utils.QueryContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class EnhancedActionToolHandler implements EnhancedToolHandler, InitializingBean {
    private static final Map<String, QueryProcessor> actionToolMap = new ConcurrentHashMap<>();
    @Resource
    ApplicationContext applicationContext;

    @Override
    public boolean handler(StepDefinition step, QueryContext context) {
        QueryProcessor queryProcessor = actionToolMap.get(step.getSubtype());
        queryProcessor.processor(step, context);
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        actionToolMap.put(ActionToolDispatchEnum.ACTION_CHECKIN.getMessage(), applicationContext.getBean(ActionCheckinProcessor.class));
        actionToolMap.put(ActionToolDispatchEnum.ACTION_SCHEDULE.getMessage(), applicationContext.getBean(ActionScheduleProcessor.class));
        actionToolMap.put(ActionToolDispatchEnum.ACTION_CANCEL.getMessage(), applicationContext.getBean(ActionCancelProcessor.class));
        actionToolMap.put(ActionToolDispatchEnum.ACTION_SHARE.getMessage(), applicationContext.getBean(ActionShareProcessor.class));
        actionToolMap.put(ActionToolDispatchEnum.ACTION_DOWNLOAD.getMessage(), applicationContext.getBean(ActionDownloadProcessor.class));
        actionToolMap.put(ActionToolDispatchEnum.ACTION_SUMMARY.getMessage(), applicationContext.getBean(ActionSummaryProcessor.class));
    }
}
