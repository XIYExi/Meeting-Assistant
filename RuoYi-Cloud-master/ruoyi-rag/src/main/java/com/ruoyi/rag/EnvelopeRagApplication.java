package com.ruoyi.rag;


import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCustomConfig
@EnableRyFeignClients
public class EnvelopeRagApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnvelopeRagApplication.class, args);
    }
}
