package com.ruoyi.agent.controller;

import com.ruoyi.agent.component.HNComponent;
import com.ruoyi.agent.constant.AgentRequestPath;
import com.ruoyi.agent.utils.SignUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/agent")
@AllArgsConstructor
public class ChatController {

    private HNComponent hnComponent;

    private RestTemplate restTemplate;

    private static final String agentId = "bbbb5af3-b5c7-4483-8251-009e85e53e24";

    @GetMapping("/chat")
    public AjaxResult chat(@RequestParam(value = "question") String question) throws Exception {
        String executeUrl = AgentRequestPath.generateRequestPath(AgentRequestPath.agentExecuteUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("appKey", hnComponent.getAppKey());
        headers.add("sign", SignUtils.getSign(hnComponent.getAppKey(), hnComponent.getAppSecret()));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sid", UUID.randomUUID().toString());
        requestBody.put("id", agentId);
        requestBody.put("input", question);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(executeUrl, requestEntity, Map.class);

        return AjaxResult.success(responseEntity);
    }



}
