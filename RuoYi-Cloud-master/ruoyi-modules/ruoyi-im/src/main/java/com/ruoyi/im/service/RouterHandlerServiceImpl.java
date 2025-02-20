package com.ruoyi.im.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.utils.uuid.UUID;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class RouterHandlerServiceImpl implements IRouterHandlerService{

    @Resource
    private IMsgAckCheckService msgAckCheckService;


    @Override
    public void onReceive(ImMsgBody imMsgBody) {
        if (sendMsgToClient(imMsgBody)) {
            // 当im服务器推送消息给客户端之后，然后需要记录下ack
            msgAckCheckService.recordMsgAck(imMsgBody, 1);
            msgAckCheckService.sendDelayMsg(imMsgBody);
        }
    }

    @Override
    public boolean sendMsgToClient(ImMsgBody imMsgBody) {
        long userId = imMsgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx!= null) {
            String msgId = UUID.randomUUID().toString();
            imMsgBody.setMsgId(msgId);
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(imMsgBody));
            ctx.writeAndFlush(respMsg);
            return true;
        }
        return false;
    }
}
