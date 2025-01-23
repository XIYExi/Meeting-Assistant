package com.ruoyi.agent.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.agent.component.HNComponent;
import com.ruoyi.agent.constant.AgentRequestPath;
import com.ruoyi.agent.constant.ConstValuePool;
import com.ruoyi.agent.domain.MessagesLogs;
import com.ruoyi.agent.entity.AgentResult;
import com.ruoyi.agent.entity.MessageDas;
import com.ruoyi.agent.entity.MessageResponseDas;
import com.ruoyi.agent.service.AgentChatService;
import com.ruoyi.agent.tcp.server.WebSocketServerHandler;
import com.ruoyi.agent.utils.SignUtils;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

@Service
public class AgentChatServiceImpl implements AgentChatService {

    @Autowired
    private HNComponent hnComponent;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    MessagesLogsServiceImpl messagesLogsService;

    private static final String agentId = "bbbb5af3-b5c7-4483-8251-009e85e53e24";


    private WebSocketServerHandler nettyServerHandler = new WebSocketServerHandler();
    @Autowired
    private RefreshEndpoint refreshEndpoint;


    @Override
    public String sendFluxMsg(String uid, String prompt) throws Exception {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Map<String, Object> bodyJson = new HashMap<>();

        // 存最新的数据
        MessagesLogs messagesLogsSave = new MessagesLogs();
        messagesLogsSave.setUserId(loginUser.getUserid().toString());
        messagesLogsSave.setContent(prompt);
        messagesLogsSave.setRole("user");
        messagesLogsSave.setCreateTime(DateUtils.getNowDate());
        messagesLogsService.insertMessagesLogs(messagesLogsSave);


        // 拼接发送格式
        bodyJson.put("sid", UUID.randomUUID().toString());
        bodyJson.put("id", agentId);
        bodyJson.put("stream", true);
        bodyJson.put("input", prompt);

        String executeUrl = AgentRequestPath.generateRequestPath(AgentRequestPath.agentExecuteUrl);

        Flux<String> chatResponseFlux = ConstValuePool.PROXY_HENG_NAO_CLIENT
                .post()
                .uri(executeUrl)
                .header("appKey", hnComponent.getAppKey())
                .header("sign", SignUtils.getSign(hnComponent.getAppKey(), hnComponent.getAppSecret()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bodyJson)
                .retrieve()
                .bodyToFlux(String.class); // 得到string返回，便于查看结束标志
        StringBuilder resultBuilder = new StringBuilder();
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
                System.out.println(res);
                if (res != null) {
                    resultBuilder.append(res);
                    nettyServerHandler.sendMsg(null, uid + "&" + resultBuilder.toString());
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
        messagesLogsSaveAnswer.setUserId(loginUser.getUserid().toString());
        messagesLogsSaveAnswer.setContent(ResMsg);
        messagesLogsSaveAnswer.setRole("assistant");
        messagesLogsSaveAnswer.setCreateTime(new Date());
        messagesLogsService.insertMessagesLogs(messagesLogsSaveAnswer);

        return ResMsg;
    }
}
