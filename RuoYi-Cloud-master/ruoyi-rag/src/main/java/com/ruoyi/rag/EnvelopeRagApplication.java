package com.ruoyi.rag;


import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.rag.assistant.declare.EnhancedStepDispatchFactory;
import com.ruoyi.rag.assistant.entity.StepDefinition;
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

    @PostConstruct
    public void test() {
        String str = "[\n" +
                "    {\n" +
                "        \"step\": 1,\n" +
                "        \"intent\": \"query\",\n" +
                "        \"subtype\": \"meeting\",\n" +
                "        \"db\": \"meeting\",\n" +
                "        \"dependency\": -1,\n" +
                "        \"data_bindings\": {},\n" +
                "        \"filters\": [\n" +
                "            {\n" +
                "                \"field\": \"title\",\n" +
                "                \"operator\": \"LIKE\",\n" +
                "                \"value\": \"网络安全大会\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"time_constraints\": {\n" +
                "            \"max_execution_ms\": 5000\n" +
                "        },\n" +
                "        \"auth_condition\": {\n" +
                "            \"required_level\": \"user\",\n" +
                "            \"verify_mode\": \"password\"\n" +
                "        }\n" +
                "    }\n" +
                "]";
        List<StepDefinition> stepDefinitions = JSONArray.parseArray(str, StepDefinition.class);
        RequestContextHolder.setRequestList(stepDefinitions);
        String dispatch = dispatchFactory.dispatch(stepDefinitions, "1");
    }
}
