package com.ruoyi.rag.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.MessageDTO;
import com.ruoyi.rag.config.ImMsgBizCodeEnum;
import com.ruoyi.rag.declare.SimpleMsgHandler;
import com.ruoyi.router.api.RemoteRouterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class AgentChatMsgHandler implements SimpleMsgHandler {
     private Logger logger = LoggerFactory.getLogger(AgentChatMsgHandler.class);
    @Resource
    private RemoteRouterService remoteRouterService;

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

        AjaxResult ajaxResult =remoteRouterService.sendMsg(imMsgBody.getUserId(), JSONObject.toJSONString(respMsg));
        logger.info("rpc send msg to router server result: {}", ajaxResult);
    }
}
