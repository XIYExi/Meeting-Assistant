package com.ruoyi.live.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.common.entity.im.MessageDTO;
import com.ruoyi.live.domain.MeetingChat;
import com.ruoyi.live.service.LiveRoomService;
import com.ruoyi.live.service.MessageChatService;
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

    @Resource
    private MessageChatService messageChatService;


    @Override
    public void handler(ImMsgBody imMsgBody) {
        // 聊天室 一个人发送，N个人接受
        MessageDTO messageDTO = JSON.parseObject(imMsgBody.getData(), MessageDTO.class);
        // System.err.println(messageDTO);
        Integer roomId = messageDTO.getRoomId();
        // 1. 消息存储
        MeetingChat messageChat = new MeetingChat()
                .setRoomId(messageDTO.getRoomId().longValue())
                .setUserId(messageDTO.getUserId())
                .setMeetingId(0L)
                .setContent(messageDTO.getContent())
                .setCreateTime(DateUtils.getNowDate())
                .setNickName(messageDTO.getAvatarName());
        messageChatService.save(messageChat);

        // 2. 聊天室推送
        List<Long> userIdList = liveRoomService.queryByRoomId(roomId, imMsgBody.getAppId());
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
