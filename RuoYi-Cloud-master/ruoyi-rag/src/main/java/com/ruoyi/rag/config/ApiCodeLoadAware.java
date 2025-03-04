package com.ruoyi.rag.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiCodeLoadAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConstValuePool.PROXY_HENG_NAO_CLIENT = WebClient.builder()
                .baseUrl("")
                .build();
    }
}
