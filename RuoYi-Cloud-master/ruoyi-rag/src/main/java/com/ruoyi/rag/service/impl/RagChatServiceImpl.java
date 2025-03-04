package com.ruoyi.rag.service.impl;

import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.rag.config.ConstValuePool;
import com.ruoyi.rag.config.RagParamConfig;
import com.ruoyi.rag.config.RagRequestPath;
import com.ruoyi.rag.domain.MessagesLogs;
import com.ruoyi.rag.entity.AgentResult;
import com.ruoyi.rag.entity.MessageResponseDas;
import com.ruoyi.rag.mapper.MessageLogsMapper;
import com.ruoyi.rag.service.MessageLogsService;
import com.ruoyi.rag.service.RagChatService;
import com.ruoyi.rag.tcp.server.WebSocketServerHandler;
import com.ruoyi.rag.utils.SignUtils;
import com.ruoyi.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;

@Service
public class RagChatServiceImpl implements RagChatService {
    private static final Logger logger = LoggerFactory.getLogger(RagChatServiceImpl.class);

    @Resource
    private RagParamConfig ragParamConfig;
    @Resource
    MessageLogsMapper messageLogsMapper;
    private static final String agentId = "d072b0c8-47e7-44cb-b250-dff3d26902f7";
    private WebSocketServerHandler nettyServerHandler = new WebSocketServerHandler();

    @Override
    public String sendFluxMsg(String uid, String text, String question) throws Exception {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Map<String, Object> bodyJson = new HashMap<>();

        // 存最新的数据
        MessagesLogs messagesLogsSave = new MessagesLogs();
        messagesLogsSave.setUserId(loginUser.getUserid().toString());
        messagesLogsSave.setContent(question);
        messagesLogsSave.setRole("user");
        messagesLogsSave.setCreateTime(DateUtils.getNowDate());
        messageLogsMapper.insert(messagesLogsSave);

        logger.info("插入用户聊天记录，持久化成功");

        // 拼接发送格式
        bodyJson.put("sid", UUID.randomUUID().toString());
        bodyJson.put("id", agentId);
        bodyJson.put("stream", true);
        bodyJson.put("input", text);

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
        messageLogsMapper.insert(messagesLogsSaveAnswer);

        return ResMsg;
    }
}
