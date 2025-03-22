package com.ruoyi.rag;


import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.rag.assistant.declare.EnhancedStepDispatchFactory;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@EnableCustomConfig
@EnableRyFeignClients
public class EnvelopeRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnvelopeRagApplication.class, args);
    }

    @Resource
    private EnhancedStepDispatchFactory dispatchFactory;

    // @PostConstruct
    public void test() {
        String str = "[\n" +
                "    {\n" +
                "        \"step\": 1,\n" +
                "        \"intent\": \"route\",\n" +
                "        \"subtype\": \"page\",\n" +
                "        \"db\": \"meeting\",\n" +
                "        \"dependency\": -1,\n" +
                "        \"data_bindings\": {},\n" +
                "        \"filters\": [],\n" +
                "        \"time_constraints\": {},\n" +
                "        \"auth_condition\": {\n" +
                "            \"verify_mode\": \"password\"\n" +
                "        },\n" +
                "        \"params\": {\n" +
                "            \"path\": \"/生态合作伙伴大会页面\",\n" +
                "            \"query\": \"生态合作伙伴大会\"\n" +
                "        }\n" +
                "    }\n" +
                "]";
        List<StepDefinition> stepDefinitions = JSONArray.parseArray(str, StepDefinition.class);
        RequestContextHolder.setRequestList(stepDefinitions);
        QueryContext dispatch = dispatchFactory.dispatch(stepDefinitions, "1");

    }
}
