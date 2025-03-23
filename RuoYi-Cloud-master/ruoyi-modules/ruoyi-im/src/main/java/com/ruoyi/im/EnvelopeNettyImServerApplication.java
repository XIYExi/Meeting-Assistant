package com.ruoyi.im;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableRyFeignClients
@EnableReactiveFeignClients
@SpringBootApplication
@EnableCustomConfig
public class EnvelopeNettyImServerApplication {

    public static void main(String[] args) throws Exception {
       SpringApplication springApplication = new SpringApplication(EnvelopeNettyImServerApplication.class);
       // springApplication.setWebApplicationType(WebApplicationType.NONE);
       springApplication.run(args);
    }
}
