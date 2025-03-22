package com.ruoyi.rag.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.MessageDTO;
import com.ruoyi.rag.assistant.declare.EnhancedStepDispatchFactory;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import com.ruoyi.rag.config.ImMsgBizCodeEnum;
import com.ruoyi.rag.config.RagCacheKeyBuilder;
import com.ruoyi.rag.declare.SimpleMsgHandler;
import com.ruoyi.rag.model.DomesticModel;
import com.ruoyi.rag.service.RagChatService;
import com.ruoyi.router.api.RemoteRouterService;
import dev.langchain4j.memory.ChatMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AgentChatMsgHandler implements SimpleMsgHandler {
    private Logger logger = LoggerFactory.getLogger(AgentChatMsgHandler.class);
    /* IM 依赖注入 */
    @Resource
    private RemoteRouterService remoteRouterService;
    @Resource
    private EnhancedStepDispatchFactory dispatchFactory;

    /* Agent Flux 依赖注入 */
    @Resource
    private DomesticModel domesticModel;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RagCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private RagChatService ragChatService;

    public static final Map<Long, ChatMemory> chatMemoryMap = new HashMap<>();

    @Override
    public void handler(ImMsgBody imMsgBody) {
        System.err.println("user原始问题： " + imMsgBody.getData());

        MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
        System.err.println("agent流式返回： " + messageDTO.getContent());
        Integer roomId = messageDTO.getRoomId();
        List<ImMsgBody> imMsgBodyList = new ArrayList<>();
        // TODO 需要改造
        ImMsgBody respMsg = new ImMsgBody();
        respMsg.setUserId(imMsgBody.getUserId());
        respMsg.setAppId(imMsgBody.getAppId());
        respMsg.setBizCode(ImMsgBizCodeEnum.Agent_Chat_Code.getCode());
        respMsg.setData(com.alibaba.fastjson.JSON.toJSONString(messageDTO));

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
        Object dependencyResult = dispatch.getDependencyResult(stepDefinitions.size(), Object.class);

        /* TODO 实验 Flux */
        Long userId = 1L;
        ChatMemory chatMemory = null;
        if (chatMemoryMap.containsKey(userId)) {
            chatMemory = chatMemoryMap.get(userId);
        }


        AjaxResult ajaxResult =remoteRouterService.sendMsg(imMsgBody.getUserId(), JSONObject.toJSONString(respMsg));
        logger.info("rpc send msg to router server result: {}", ajaxResult);
    }
}
