package com.ruoyi.rag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagParamConfig {

    @Value("${hengnao.appKey}")
    private String hengnaoAppKey;

    @Value("${hengnao.appSecret}")
    private String hengnaoAppSecret;

    @Value("${qwen.apiKey}")
    private String qwenAppKey;

    @Value("${qwen.textEmbedding}")
    private String qwenTextEmbedding;

    @Value("${qwen.textDimensions}")
    private Integer qwenTextDimensions;

    public String getAppKey() {
        return hengnaoAppKey;
    }

    public Integer getQwenTextDimensions() {
        return qwenTextDimensions;
    }

    public String getAppSecret() {
        return hengnaoAppSecret;
    }

    public String getQwenAppKey() {
        return qwenAppKey;
    }

    public String getQwenTextEmbedding() {
        return qwenTextEmbedding;
    }
}
