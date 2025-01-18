package com.ruoyi.agent.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HNComponent {

    @Value("${hengnao.appKey}")
    private String hengnaoAppKey;

    @Value("${hengnao.appSecret}")
    private String hengnaoAppSecret;


    public String getAppKey() {
        return hengnaoAppKey;
    }

    public String getAppSecret() {
        return hengnaoAppSecret;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
