package com.ruoyi.rag.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.rag.config.RagParamConfig;
import com.ruoyi.rag.config.RagRequestPath;
import com.ruoyi.rag.entity.QuestionEntity;
import com.ruoyi.rag.utils.SignUtils;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Component
public class DomesticModel implements ChatLanguageModel {
    @Resource
    private RagParamConfig ragParamConfig;
    @Resource
    private RestTemplate restTemplate;


    /**
     * 携带历史聊天消息进行对话，
     * 提供给可变参数和 ChatMemory 使用
     * @param list
     * @return
     */
    @Override
    public Response<AiMessage> generate(List<ChatMessage> list) {
        // 将消息列表转换为国产大模型所需的格式
        try {
            HttpEntity<Map<String, Object>> requestBody = convertToDomesticModelFormat(list);
            QuestionEntity questionEntity = callDomesticModelApi(requestBody);
            // System.err.println(questionEntity);
            // 将响应转换为 AiMessage
            AiMessage aiMessage = AiMessage.from(questionEntity.getMessage().getContent());
            // 返回 Response
            return Response.from(aiMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response<AiMessage> generate(ChatMessage... messages) {
        // 将消息列表转换为国产大模型所需的格式
        try {
            Response<AiMessage> generate = this.generate(Arrays.asList(messages));
            return generate;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 直接传递一条 message 给大模型进行对话
     * @param userMessage
     * @return
     */
    @Override
    public String generate(String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("appKey", ragParamConfig.getAppKey());
            headers.add("sign", SignUtils.getSign(ragParamConfig.getAppKey(), ragParamConfig.getAppSecret()));

            Map<String, Object> requestBody = new HashMap<>();
            // requestBody.put("sid", "a5778aec-1109-47d2-b337-c7014992870b");
            List<Map<String, Object>> message = new ArrayList<>();

            String prompt = "作为会议安全助手，你需要帮助抽取并识别以下的内容，并按照下述要求返回结果（有且仅返回下述两点内容！）：\n" +
                    "1. intent为用户意图：限定为route，action，chat。其中route表示用户想要进入或者跳转到某个指定页面；action表示用户想要执行某些操作，如查询、总结、抽取等；chat表示用户当前内容不符合上述消息，只是单纯的在和大模型进行对话。\n" +
                    "2. keywords为关键词：只对输入的内容进行信息提取，提取其中最有价值的1~2个信息，用英文逗号分隔，不要分点不要扩充，不要翻译，直接返回中文，返回用户输入中出现的内容。\n";

            Map<String, Object> messageElement = new HashMap<>();
            messageElement.put("role", "user");
            messageElement.put("content", prompt + userMessage);
            messageElement.put("seq", 0);
            messageElement.put("type", "text");
            message.add(messageElement);

            requestBody.put("message", message);
            // Long userId = SecurityUtils.getUserId();
            Long userId = 1L;
            requestBody.put("uid", String.valueOf(userId));
            Map<String, String> intentMap = new HashMap<>();
            intentMap.put("intent", "chat");
            requestBody.put("parameter", intentMap);


            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            QuestionEntity questionEntity = callDomesticModelApi(requestEntity);
            return questionEntity.getMessage().getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






    /**
     *  携带历史数据，把 List<ChatMessage> 批量转换成恒脑接受的 Map 类型
     * @param userMessage
     * @return
     * @throws Exception
     */
    private HttpEntity<Map<String, Object>> convertToDomesticModelFormat(List<ChatMessage> userMessage) throws Exception {
        // 将 LangChain4j 的 ChatMessage 转换为恒脑大模型所需的格式
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("appKey", ragParamConfig.getAppKey());
        headers.add("sign", SignUtils.getSign(ragParamConfig.getAppKey(), ragParamConfig.getAppSecret()));

        Map<String, Object> requestBody = new HashMap<>();
        String prompt = "作为会议安全助手，你需要帮助抽取并识别以下的内容，并按照下述要求返回结果（有且仅返回下述两点内容！）：\n" +
                "1. intent为用户意图：限定为route，action，chat。其中route表示用户想要进入或者跳转到某个指定页面；action表示用户想要执行某些操作，如查询、总结、抽取等；chat表示用户当前内容不符合上述消息，只是单纯的在和大模型进行对话。\n" +
                "2. keywords为关键词：只对输入的内容进行信息提取，提取其中最有价值的1~2个信息，用英文逗号分隔，不要分点不要扩充，不要翻译，直接返回中文，返回用户输入中出现的内容。\n";

        // requestBody.put("sid", "a5778aec-1109-47d2-b337-c7014992870b");
        List<Map<String, Object>> message= new ArrayList<>();
        for (int i = 0; i < userMessage.size(); ++i) {

            if (i == userMessage.size() - 1) {
                Map<String, Object> lastElement = new HashMap<>();
                lastElement.put("role", "user");
                lastElement.put("content", prompt + userMessage.get(i).text());
                lastElement.put("seq", 0);
                lastElement.put("type", "text");
                message.add(lastElement);
                break;
            }
            ChatMessage item = userMessage.get(i);
            Map<String, Object> messageElement = new HashMap<>();
            messageElement.put("role", item.type().toString().toLowerCase().equals("ai") ? "assistant" : "user");
            messageElement.put("content", item.text());
            messageElement.put("seq", 0);
            messageElement.put("type", "text");
            message.add(messageElement);
        }
        message.get(message.size() - 1).put("content", prompt + message.get(message.size() - 1).get("content"));
        requestBody.put("message", message);
        requestBody.put("uid", "1");
        Map<String, String> intentMap = new HashMap<>();
        intentMap.put("intent", "chat");
        requestBody.put("parameter", intentMap);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        return requestEntity;
    }


    /**
     * 使用 RestTemplate 调用恒脑大模型 API
     * @param requestEntity
     * @return
     */
    private QuestionEntity callDomesticModelApi(HttpEntity<Map<String, Object>> requestEntity) {
        // 使用 RestTemplate 调用恒脑大模型 API
        String executeUrl = RagRequestPath.generateRequestPath(RagRequestPath.ragExecuteUrl);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(executeUrl, requestEntity, Map.class);
        return convertEntityToMessage(responseEntity);
    }

    /**
     * 将 RestTemplate 返回的信息解析成恒脑的 Question 类型
     * @param responseEntity
     * @return
     */
    public static QuestionEntity convertEntityToMessage(ResponseEntity<Map> responseEntity) {
        Map body = responseEntity.getBody();
        Map responseWrapper = JSON.parseObject(JSONObject.toJSONString(body), Map.class);
        QuestionEntity data = JSON.parseObject(JSONObject.toJSONString(responseWrapper.get("data")), QuestionEntity.class);
        return data;
    }

}
