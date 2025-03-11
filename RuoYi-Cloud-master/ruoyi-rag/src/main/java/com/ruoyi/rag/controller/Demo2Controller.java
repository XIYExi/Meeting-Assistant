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
import org.aspectj.weaver.loadtime.Aj;
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
                "        \"intent\": \"route\",\n" +
                "        \"params\": {\n" +
                "            \"keywords\": \"会议详细页面\",\n" +
                "            \"db\": \"\",\n" +
                "            \"filters\": [\n" +
                "                { \"filter\": \"title\", \"value\": \"恒脑网络安全大会\", \"order\": \"\", \"operator\": \"like\" }\n" +
                "            ],\n" +
                "            \"dependency\": -1\n" +
                "        }\n" +
                "    }\n" +
                "]";
        // 分割出来的每一个step
        List<StepSplitEntity> steps = JSONArray.parseArray(result, StepSplitEntity.class);



        return AjaxResult.success(steps);
    }
}
