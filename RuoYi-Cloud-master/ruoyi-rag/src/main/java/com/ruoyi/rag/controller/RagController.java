package com.ruoyi.rag.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.config.RagParamConfig;
import com.ruoyi.rag.config.RagRequestPath;
import com.ruoyi.rag.utils.SignUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {

    private static final String agentId = "bbbb5af3-b5c7-4483-8251-009e85e53e24";

    @Resource
    private RagParamConfig ragParamConfig;
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/chat")
    public AjaxResult chat(@RequestParam(value = "question") String question) throws Exception {
        String executeUrl = RagRequestPath.generateRequestPath(RagRequestPath.agentExecuteUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("appKey", ragParamConfig.getAppKey());
        headers.add("sign", SignUtils.getSign(ragParamConfig.getAppKey(), ragParamConfig.getAppSecret()));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sid", "a5778aec-1109-47d2-b337-c7014992870b");
        requestBody.put("id", agentId);
        requestBody.put("input", question);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(executeUrl, requestEntity, Map.class);

        return AjaxResult.success(responseEntity);
    }


}
