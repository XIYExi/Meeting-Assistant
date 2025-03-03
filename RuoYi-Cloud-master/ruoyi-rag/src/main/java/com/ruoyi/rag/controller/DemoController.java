package com.ruoyi.rag.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.assistant.Assistant;
import com.ruoyi.rag.config.RagCacheKeyBuilder;
import com.ruoyi.rag.config.RagParamConfig;
import com.ruoyi.rag.config.RagRequestPath;
import com.ruoyi.rag.entity.QuestionEntity;
import com.ruoyi.rag.model.CustomChatMemory;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.model.DomesticModel;
import com.ruoyi.rag.utils.SignUtils;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private RagParamConfig ragParamConfig;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private DomesticModel domesticModel;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RagCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;

    @Deprecated
    @GetMapping("/query")
    public AjaxResult query() throws Exception {
        String executeUrl = RagRequestPath.generateRequestPath(RagRequestPath.ragExecuteUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("appKey", ragParamConfig.getAppKey());
        headers.add("sign", SignUtils.getSign(ragParamConfig.getAppKey(), ragParamConfig.getAppSecret()));

        Map<String, Object> requestBody = new HashMap<>();
        String prompt = "作为会议安全助手，你需要帮助抽取并识别以下的内容，并按照下述要求返回结果（有且仅返回下述两点内容！）：\n" +
                "1. intent为用户意图：限定为route，action，chat。其中route表示用户想要进入或者跳转到某个指定页面；action表示用户想要执行某些操作，如查询、总结、抽取等；chat表示用户当前内容不符合上述消息，只是单纯的在和大模型进行对话。\n" +
                "2. keywords为关键词：只对输入的内容进行信息提取，提取其中最有价值的1~2个信息，用英文逗号分隔，不要分点不要扩充，不要翻译，直接返回中文，返回用户输入中出现的内容。\n";
        Map<String, Object> messageElement = new HashMap<>();
        messageElement.put("role", "user");
        // messageElement.put("content", prompt + "告诉我教育系统数据安全专题会议中AI如何“加”网络安全部分的主要内容？");
        // messageElement.put("content", prompt + "告诉我恒脑大模型发布会上宇凡关于大模型生态发展的汇报内容");
        // messageElement.put("content", prompt + "跳转到会议详细页面");
        messageElement.put("content", prompt + "我是小明，你是我最好的朋友小强，你明白了吗？");
        messageElement.put("seq", 0);
        messageElement.put("type", "text");

        List<Map<String, Object>> message= new ArrayList<>();
        message.add(messageElement);
        requestBody.put("message", message);
        requestBody.put("uid", "1");
        Map<String, String> intentMap = new HashMap<>();
        intentMap.put("intent", "chat");
        requestBody.put("parameter", intentMap);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(executeUrl, requestEntity, Map.class);

        QuestionEntity questionEntity = convertEntityToMessage(responseEntity);

        return AjaxResult.success(questionEntity);
    }

    @GetMapping("/custom")
    public AjaxResult custom() {
        // 创建 Assistant
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(domesticModel)
                .build();
        String response = assistant.chat("你好，国产大模型！");
        return AjaxResult.success(response);
    }

    @GetMapping("/chat")
    public AjaxResult chat() {
        String result = domesticModel.generate("告诉我关于西湖论剑大会的消息");
        return AjaxResult.success(result);
    }

    @GetMapping("/userMessage")
    public AjaxResult userMessage() {
        UserMessage usermessage = UserMessage.from("你好");
        Response<AiMessage> generate = domesticModel.generate(usermessage);
        System.err.println(generate);
        AiMessage content = generate.content();
        return AjaxResult.success(content.text());
    }

    @GetMapping("/history")
    public AjaxResult history() {
        UserMessage userRequest = UserMessage.from("你好");
        AiMessage aiResponse = AiMessage.from("你好！我是小恒，安恒信息的网络安全问答助手。有什么网络安全问题我可以帮助你解答吗？");
        UserMessage userRequestAgain = UserMessage.from("那么我是谁呢？");
        Response<AiMessage> generate = domesticModel.generate(userRequest, aiResponse, userRequestAgain);
        return AjaxResult.success(generate.content().text());
    }

    @GetMapping("/chatMemory")
    public AjaxResult chatMemory() {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
        chatMemory.add(UserMessage.from("告诉我教育系统数据安全专题会议中AI如何“加”网络安全部分的主要内容？"));
        AiMessage answer = domesticModel.generate(chatMemory.messages()).content();
        chatMemory.add(answer);

        chatMemory.add(UserMessage.from("告诉我教育系统数据安全专题会议中AI如何“加”网络安全部分的主要内容？"));
        AiMessage content = domesticModel.generate(chatMemory.messages()).content();
        chatMemory.add(content);

        chatMemory.add(UserMessage.from("跳转到西湖论剑大会详细页面"));
        AiMessage result = domesticModel.generate(chatMemory.messages()).content();
        chatMemory.add(result);

        return AjaxResult.success(result.text());
    }

    @GetMapping("/customChatMemory")
    public AjaxResult customChatMemory() {
        ChatMemory chatMemory = new CustomChatMemory(1L, redisTemplate, cacheKeyBuilder);

        chatMemory.add(UserMessage.from("告诉我教育系统数据安全专题会议中AI如何“加”网络安全部分的主要内容？"));
        AiMessage answer = domesticModel.generate(chatMemory.messages()).content();
        chatMemory.add(answer);

        chatMemory.add(UserMessage.from("跳转到西湖论剑大会详细页面"));
        AiMessage result = domesticModel.generate(chatMemory.messages()).content();
        chatMemory.add(result);

        return AjaxResult.success(result.text());
    }

    @GetMapping("/embeddings")
    public AjaxResult embeddings() {
        String executeUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1/embeddings";
         String apiKey = "sk-47912a5b978c442c8b21e8c6a41324d4";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-embedding-v2");
        requestBody.put("input", "风急天高猿啸哀，渚清沙白鸟飞回，无边落木萧萧下，不尽长江滚滚来");
        requestBody.put("dimensions", 1024);
        requestBody.put("encoding_format", "float");


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(executeUrl, requestEntity, Map.class);

        Map result = responseEntity.getBody();

        return AjaxResult.success(result);
    }

    @GetMapping("/envelopeEmbeddings")
    public AjaxResult envelopeEmbeddings() {
        // EmbeddingModel 封装Qwen Api
        TextSegment segment = TextSegment.from("网络安全大会");
        Response<List<Embedding>> listResponse = domesticEmbeddingModel.embedAll(Collections.singletonList(segment));
        List<Embedding> content = listResponse.content();
        List<Float> floats = content.get(0).vectorAsList();
        return AjaxResult.success(floats);
    }


    public static QuestionEntity convertEntityToMessage(ResponseEntity<Map> responseEntity) {
        Map body = responseEntity.getBody();
        Map responseWrapper = JSON.parseObject(JSONObject.toJSONString(body), Map.class);
        QuestionEntity data = JSON.parseObject(JSONObject.toJSONString(responseWrapper.get("data")), QuestionEntity.class);
        return data;
    }

}
