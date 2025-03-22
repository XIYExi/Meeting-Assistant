package com.ruoyi.rag.assistant.handler;

import com.ruoyi.rag.assistant.constant.RouteToolDispatchEnum;
import com.ruoyi.rag.assistant.declare.EnhancedToolHandler;
import com.ruoyi.rag.assistant.declare.QueryProcessor;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.handler.route.RouteGeoProcessor;
import com.ruoyi.rag.assistant.handler.route.RoutePageProcessor;
import com.ruoyi.rag.assistant.utils.QueryContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EnhancedRouteToolHandler implements EnhancedToolHandler, InitializingBean {

    private static final Map<String, QueryProcessor> routeToolMap = new ConcurrentHashMap<>();

    @Resource
    ApplicationContext applicationContext;

    @Override
    public boolean handler(StepDefinition step, QueryContext context) {
        QueryProcessor queryProcessor = routeToolMap.get(step.getSubtype());
        queryProcessor.processor(step, context);
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        routeToolMap.put(RouteToolDispatchEnum.ROUTE_PAGE.getMessage(), applicationContext.getBean(RoutePageProcessor.class));
        routeToolMap.put(RouteToolDispatchEnum.ROUTE_GEO.getMessage(), applicationContext.getBean(RouteGeoProcessor.class));
    }
}
