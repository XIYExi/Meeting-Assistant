package com.ruoyi.rag.controller;


import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.rag.config.RagCacheKeyBuilder;
import com.ruoyi.rag.declare.ToolDispatchFactory;
import com.ruoyi.rag.domain.StepSplitEntity;
import com.ruoyi.rag.entity.ChatReq;
import com.ruoyi.rag.model.DomesticModel;
import com.ruoyi.rag.service.RagChatService;
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rag")
public class Demo2Controller {
    private static final Logger logger = LoggerFactory.getLogger(Demo2Controller.class);
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
    private WebSocketServerHandler nettyServerHandler = new WebSocketServerHandler();

    @PostMapping("/v2/flux")
    @ResponseBody
    public AjaxResult flux_v2(@RequestBody ChatReq req) throws Exception {
        String question = req.getText();
        String uid = req.getUid();

        String result = "[\n" +
                "    {\n" +
                "        \"step\": 1,\n" +
                "        \"intent\": \"query\",\n" +
                "        \"params\": {\n" +
                "            \"keywords\": \"教育系统数据安全专题会议\",\n" +
                "            \"db\": \"meeting\",\n" +
                "            \"filters\": [\n" +
                "                { \"filter\": \"title\", \"value\": \"教育系统数据安全专题会议\", \"order\": \"\", \"operator\": \"like\" }\n" +
                "            ],\n" +
                "            \"dependency\": -1\n" +
                "        }\n" +
                "    }\n" +
                "]";

        // 分割出来的每一个step
        List<StepSplitEntity> steps = JSONArray.parseArray(result, StepSplitEntity.class);
        String intentReturn = "用户提问【"+question+"】,通过拆解用户意图可知，拆解需求后需要执行【" + steps.size() +"】步骤\n";
        nettyServerHandler.sendMsg(null, uid + "&^intent" + intentReturn);

        String finalUserPrompt = toolDispatchFactory.dispatch(steps, uid);
        logger.info("===== RAG v2 前置工具链处理完成! =====");

        String finalQuestion = question + "\n现在有信息：\n" + finalUserPrompt + "。结合上述内容，并回答问题!不需要重复问题和重读信息内容!只回答一次，不要重复回答！";
        System.err.println(finalQuestion);
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
