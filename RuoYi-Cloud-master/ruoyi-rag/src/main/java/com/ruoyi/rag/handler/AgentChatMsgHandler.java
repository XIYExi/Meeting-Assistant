package com.ruoyi.rag.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.MessageDTO;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.rag.assistant.declare.EnhancedStepDispatchFactory;
import com.ruoyi.rag.assistant.entity.StepDefinition;
import com.ruoyi.rag.assistant.utils.QueryContext;
import com.ruoyi.rag.assistant.utils.RequestContextHolder;
import com.ruoyi.rag.config.*;
import com.ruoyi.rag.declare.SimpleMsgHandler;
import com.ruoyi.rag.domain.MessagesLogs;
import com.ruoyi.rag.entity.AgentResult;
import com.ruoyi.rag.entity.MessageResponseDas;
import com.ruoyi.rag.mapper.MessageLogsMapper;
import com.ruoyi.rag.model.CustomChatMemory;
import com.ruoyi.rag.model.DomesticModel;
import com.ruoyi.rag.service.RagChatService;
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
import com.ruoyi.rag.utils.SignUtils;
import com.ruoyi.router.api.RemoteRouterService;
import com.ruoyi.system.api.model.LoginUser;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Semaphore;

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

        ChatMemory chatMemory = null;
        if (chatMemoryMap.containsKey(imMsgBody.getUserId())) {
            chatMemory = chatMemoryMap.get(imMsgBody.getUserId());
        } else {
            chatMemory = new CustomChatMemory(imMsgBody.getUserId(), redisTemplate, cacheKeyBuilder);
            chatMemoryMap.put(imMsgBody.getUserId(), chatMemory);
        }
        String question = imMsgBody.getData();
        // 先把现在用户的question送进去
        chatMemory.add(UserMessage.from(question));

        StringBuffer sb = new StringBuffer();
        try {
            String text = "用户原始提问：" + question + " 提供了一组数据： " + JSONObject.toJSONString(dependencyResult);
            String msg = sendFluxMsg(imMsgBody.getMsgId(), text, question, imMsgBody);
            sb.append(msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        chatMemory.add(AiMessage.from(sb.toString()));
    }


    @Resource
    private RagParamConfig ragParamConfig;
    @Resource
    MessageLogsMapper messageLogsMapper;
    private static final String agentId = "d072b0c8-47e7-44cb-b250-dff3d26902f7";

    public String sendFluxMsg(String uid, String text, String question, ImMsgBody imMsgBody) throws Exception {
        Map<String, Object> bodyJson = new HashMap<>();

        System.err.println("1");

        // 存最新的数据
        MessagesLogs messagesLogsSave = new MessagesLogs();
        messagesLogsSave.setUserId(String.valueOf(imMsgBody.getUserId()));
        messagesLogsSave.setContent(question);
        messagesLogsSave.setRole("user");
        messagesLogsSave.setCreateTime(DateUtils.getNowDate());
        messageLogsMapper.insert(messagesLogsSave);

        System.err.println("2");

        logger.info("插入用户聊天记录，持久化成功");

         // 拼接发送格式
        bodyJson.put("sid", UUID.randomUUID().toString());
        bodyJson.put("id", agentId);
        bodyJson.put("stream", true);
        bodyJson.put("input", text);

        System.err.println("3");

        String executeUrl = RagRequestPath.generateRequestPath(RagRequestPath.agentExecuteUrl);
        Flux<String> chatResponseFlux = ConstValuePool.PROXY_HENG_NAO_CLIENT
                .post()
                .uri(executeUrl)
                .header("appKey", ragParamConfig.getAppKey())
                .header("sign", SignUtils.getSign(ragParamConfig.getAppKey(), ragParamConfig.getAppSecret()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJson)
                .retrieve()
                .bodyToFlux(String.class); // 得到string返回，便于查看结束标志
        StringBuilder resultBuilder = new StringBuilder();
        StringBuilder basicLlmBuilder = new StringBuilder();
        // 设置同步信号量
        Semaphore semaphore = new Semaphore(0);
        chatResponseFlux.subscribe(value -> {
            // 获得数据，拼接结果，发送给前端
            try {
                MessageResponseDas chatResponse = JSONUtil.toBean(value, MessageResponseDas.class);
                AgentResult agentResult = chatResponse.getData();
                if (agentResult.getContent() == null){
                    return;
                }
                String res = agentResult.getContent();
                if (res != null) {
                    if (!agentResult.getFrom().equals("execute_result")){
                        basicLlmBuilder.append(res);
                        // System.err.println(res);
                        // nettyServerHandler.sendMsg(null, uid + "&^llm" + basicLlmBuilder.toString());
                        sendMsgByIm(imMsgBody, "&^llm" + basicLlmBuilder.toString());
                    }
                    else {
                        // System.out.println(res);
                        resultBuilder.append(res);
                        // nettyServerHandler.sendMsg(null, uid + "&" + resultBuilder.toString());
                        sendMsgByIm(imMsgBody, "&" + resultBuilder.toString());
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            // 失败释放信号量，并报错
            semaphore.release();
            throw new RuntimeException("恒脑Agent执行出错", error);
        }, semaphore::release);// 成功释放信号量
        semaphore.acquire();
        String ResMsg = resultBuilder.toString();

        MessagesLogs messagesLogsSaveAnswer = new MessagesLogs();
        messagesLogsSaveAnswer.setUserId(String.valueOf(imMsgBody.getUserId()));
        messagesLogsSaveAnswer.setContent(ResMsg);
        messagesLogsSaveAnswer.setRole("assistant");
        messagesLogsSaveAnswer.setCreateTime(new Date());
        messageLogsMapper.insert(messagesLogsSaveAnswer);

        // 这个地方单独发送一条消息，告诉前端over，这样可以进行下一段对话
        sendMsgByIm(imMsgBody, "&^over");

        return ResMsg;
    }

    private void sendMsgByIm(ImMsgBody imMsgBody, String msg) throws Exception {
        ImMsgBody respMsg = new ImMsgBody();
        respMsg.setUserId(imMsgBody.getUserId());
        respMsg.setAppId(imMsgBody.getAppId());
        respMsg.setBizCode(ImMsgBizCodeEnum.Agent_Chat_Code.getCode());
        respMsg.setData(com.alibaba.fastjson.JSON.toJSONString(msg));
        Mono<AjaxResult> ajaxResult = remoteRouterService.sendMsg(imMsgBody.getUserId(), JSONObject.toJSONString(respMsg));
        ajaxResult.subscribe(res -> {
            logger.info("rpc send msg to router server result: {}", res);
        });
    }


}
