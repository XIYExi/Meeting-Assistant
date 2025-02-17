package com.ruoyi.live.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.router.api.RemoteRouterService;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.live.entity.ImMsgBody;
import com.ruoyi.live.entity.MessageDTO;
import com.ruoyi.live.enums.ImMsgBizCodeEnum;
import com.ruoyi.live.handler.SimpleMsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 直播间im聊天消息
 */
@Component
public class LiveRoomMsgHandler implements SimpleMsgHandler {

    private Logger logger = LoggerFactory.getLogger(LiveRoomMsgHandler.class);
    @Resource
    private RemoteRouterService remoteRouterService;


    @Override
    public void handler(ImMsgBody imMsgBody) {
        MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
        // TODO 直播间业务时候继续修改
        ImMsgBody respMsg = new ImMsgBody();
        respMsg.setUserId(messageDTO.getObjectId());
        respMsg.setAppId(imMsgBody.getAppId());
        respMsg.setBizCode(ImMsgBizCodeEnum.LIVE_ROOM_IM_BIZ.getCode());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("senderId", messageDTO.getUserId());
        jsonObject.put("content", messageDTO.getContent());
        respMsg.setData(jsonObject.toJSONString());

        AjaxResult ajaxResult = remoteRouterService.sendMsg(messageDTO.getUserId(), JSON.toJSONString(respMsg));
        logger.info("rpc send msg to router server result: {}", ajaxResult);
    }
}
