package com.ruoyi.live;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class EnvelopeLiveApplication {

    public static void main(String[] args) {
        // SpringApplication.run(EnvelopeLiveApplication.class, args);
        SpringApplication springApplication = new SpringApplication(EnvelopeLiveApplication.class);
        // springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
