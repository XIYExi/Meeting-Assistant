package com.ruoyi.rag.controller;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.rag.config.RagCacheKeyBuilder;
import com.ruoyi.rag.config.RagParamConfig;
import com.ruoyi.rag.config.RagRequestPath;
import com.ruoyi.rag.declare.ToolDispatchFactory;
import com.ruoyi.rag.entity.ChatReq;
import com.ruoyi.rag.model.CustomChatMemory;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.model.DomesticModel;
import com.ruoyi.rag.service.RagChatService;
import com.ruoyi.rag.utils.SignUtils;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {

    private static final Logger logger = LoggerFactory.getLogger(RagController.class);

    @Resource
    private ToolDispatchFactory toolDispatchFactory;
    @Resource
    private DomesticModel domesticModel;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RagCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RagChatService ragChatService;

    public static final Map<Long, ChatMemory> chatMemoryMap = new HashMap<>();


    @PostMapping("/flux")
    @ResponseBody
    public AjaxResult chat(@RequestBody ChatReq req) throws Exception {
        String question = req.getText();

        // Long userId = SecurityUtils.getUserId();
        Long userId = 1L;
        ChatMemory chatMemory = null;
        if (chatMemoryMap.containsKey(userId)) {
            chatMemory = chatMemoryMap.get(userId);
        }
        else {
            chatMemory = new CustomChatMemory(userId, redisTemplate, cacheKeyBuilder);
            chatMemoryMap.put(userId, chatMemory);
        }
        chatMemory.add(UserMessage.from(question));
        String generate = domesticModel.generate(question);
        chatMemory.add(AiMessage.from(generate));

        String[] split = generate.split("\n");
        String intent = null;
        String keywords = null;
        for (int i=0;i<split.length;i++) {
            if(split[i].startsWith("intent")) {
                // 解析意图
                String[] intentSplit = split[i].split(": ");
                intent = intentSplit[1];
            }
            else {
                // 解析keywords
                String[] keywordSplit = split[i].split(": ");
                keywords = keywordSplit[1];
            }
        }
        //System.err.println(intent + " " + keywords);
        // 工具路由工厂，分发处理函数, 获取增强的prompt信息
        String prompt = toolDispatchFactory.dispatch(intent, keywords);
        logger.info("===== RAG 前置工具链处理完成! =====");

        String finalQuestion = question + "\n现在有信息：\n" + prompt + "。结合上述内容，并回答问题!不需要重复问题和重读信息内容!";

        AjaxResult ajax = AjaxResult.success("");
        try {
            String msg = ragChatService.sendFluxMsg(req.getUid(), finalQuestion, question);
            ajax = AjaxResult.success(msg);
        } catch (Exception e) {
            ajax = AjaxResult.error(e.getMessage());
        }

        return ajax;
    }


}
