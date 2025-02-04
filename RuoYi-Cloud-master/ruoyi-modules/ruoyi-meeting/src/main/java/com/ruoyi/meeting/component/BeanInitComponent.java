package com.ruoyi.meeting.component;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BeanInitComponent {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
