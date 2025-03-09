package com.ruoyi.collection;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRyFeignClients
@EnableCustomConfig
public class EnvelopeCollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnvelopeCollectionApplication.class, args);
    }
}
