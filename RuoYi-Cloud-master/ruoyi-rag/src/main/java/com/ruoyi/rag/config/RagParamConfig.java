package com.ruoyi.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagParamConfig {

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
}
