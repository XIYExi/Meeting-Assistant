package com.ruoyi.rec;


import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCustomConfig
@EnableRyFeignClients
@EnableScheduling
public class EnvelopeRecApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnvelopeRecApplication.class, args);
    }
}
