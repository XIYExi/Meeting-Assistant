package com.ruoyi.live.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.MessageDTO;
import com.ruoyi.live.service.LiveRoomService;
import com.ruoyi.router.api.RemoteRouterService;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.live.enums.ImMsgBizCodeEnum;
import com.ruoyi.live.handler.SimpleMsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播间im聊天消息
 */
@Component
public class LiveRoomMsgHandler implements SimpleMsgHandler {

    private Logger logger = LoggerFactory.getLogger(LiveRoomMsgHandler.class);
    @Resource
    private RemoteRouterService remoteRouterService;
    @Resource
    private LiveRoomService liveRoomService;


    @Override
    public void handler(ImMsgBody imMsgBody) {
        // 聊天室 一个人发送，N个人接受
        MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
        Integer roomId = messageDTO.getRoomId();
        List<Long> userIdList = liveRoomService.queryByRoomId(roomId, imMsgBody.getAppId());
        //System.err.println(userIdList.size());

        List<ImMsgBody> imMsgBodyList = new ArrayList<>();
        userIdList.forEach(userId -> {
            ImMsgBody respMsg = new ImMsgBody();
            respMsg.setUserId(userId);
            respMsg.setAppId(imMsgBody.getAppId());
            respMsg.setBizCode(ImMsgBizCodeEnum.LIVE_ROOM_IM_BIZ.getCode());
            respMsg.setData(com.alibaba.fastjson.JSON.toJSONString(messageDTO));
            imMsgBodyList.add(respMsg);
        });

        AjaxResult ajaxResult = remoteRouterService.batchSendMsg(imMsgBodyList);
        logger.info("rpc send msg to router server result: {}", ajaxResult);
    }
}
