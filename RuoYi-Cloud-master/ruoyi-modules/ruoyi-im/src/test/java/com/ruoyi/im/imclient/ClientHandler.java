package com.ruoyi.im.imclient;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ClientHandler extends ChannelInboundHandlerAdapter {
    //private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ImMsg imMsg = (ImMsg) msg;
        if (imMsg.getCode() == ImMsgCodeEnum.IM_BIZ_MSG.getCode()) {
            ImMsgBody respBody = JSON.parseObject(new String(imMsg.getBody()), ImMsgBody.class);
            ImMsgBody ackBody = new ImMsgBody();
            ackBody.setMsgId(respBody.getMsgId());
            ackBody.setAppId(respBody.getAppId());
            ackBody.setUserId(respBody.getUserId());
            ImMsg ackMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(ackBody));
            ctx.writeAndFlush(ackMsg);
        }
        System.out.println("【服务端响应数据】result is " + new String(imMsg.getBody()));
    }
}
