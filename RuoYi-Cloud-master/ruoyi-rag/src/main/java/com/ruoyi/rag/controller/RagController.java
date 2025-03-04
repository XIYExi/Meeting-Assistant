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
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
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

    private WebSocketServerHandler nettyServerHandler = new WebSocketServerHandler();


    @PostMapping("/flux")
    @ResponseBody
    public AjaxResult chat(@RequestBody ChatReq req) throws Exception {
        String question = req.getText();
        String uid = req.getUid();

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

        String intentReturn = "用户提问【"+question+"】,通过拆解用户意图可知，用户希望执行【" +  judgeType(intent) + "】操作，同时提取关键词：" + keywords + "\n\n";
        nettyServerHandler.sendMsg(null, uid + "&^intent" + intentReturn);

        //System.err.println(intent + " " + keywords);
        // 工具路由工厂，分发处理函数, 获取增强的prompt信息
        String prompt = toolDispatchFactory.dispatch(intent, keywords);
        logger.info("===== RAG 前置工具链处理完成! =====");

        String finalQuestion = question + "\n现在有信息：\n" + prompt + "。结合上述内容，并回答问题!不需要重复问题和重读信息内容!";

        String toolReturn = "智能体助理接入数据库，通过Embedding相似度匹配，查询到以下额外信息：" + prompt + "\n会务助理将集合上述信息，整合发送给恒脑大模型进行总结：\n";
        nettyServerHandler.sendMsg(null, uid + "&^tool" + toolReturn);

        AjaxResult ajax = AjaxResult.success("");
        try {
            String msg = ragChatService.sendFluxMsg(req.getUid(), finalQuestion, question);
            ajax = AjaxResult.success(msg);
        } catch (Exception e) {
            ajax = AjaxResult.error(e.getMessage());
        }

        return ajax;
    }

    private String judgeType(String intent) {
        if (intent.equals("route"))
            return "路由跳转";
        else if (intent.equals("action"))
            return "数据查询";
        else
            return "对话聊天";
    }


    public void sendLongString(String longString, String uid, String prefix) throws Exception {
    int chunkSize = 12; // 每段的大小
    int totalChunks = (longString.length() + chunkSize - 1) / chunkSize; // 总段数

    for (int i = 0; i < totalChunks; i++) {
        int start = i * chunkSize;
        int end = Math.min(start + chunkSize, longString.length());
        String chunk = longString.substring(start, end);

        // 发送消息
        nettyServerHandler.sendMsg(null, uid + "&^" + prefix + chunk);
    }
}


}
