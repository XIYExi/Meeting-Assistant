package com.ruoyi.router;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.router.service.ImRouterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
@EnableCustomConfig
@EnableRyFeignClients
public class EnvelopeImRouterApplication /*implements CommandLineRunner*/ {

    @Resource
    private ImRouterService imRouterService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EnvelopeImRouterApplication.class);
        //application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    /*@Override
    public void run(String... args) throws Exception {
        for(int i=0;i<1000;++i) {
            ImMsgBody imMsgBody = new ImMsgBody();
            imRouterService.sendMsg(1001L, JSON.toJSONString(imMsgBody));
            Thread.sleep(1000);
        }
    }*/
}
