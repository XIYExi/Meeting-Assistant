package com.ruoyi.im.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.entity.im.ImMsgBody;
import com.ruoyi.im.common.ImContextUtils;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.handler.SimpleHandler;
import com.ruoyi.im.service.IMsgAckCheckService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接收到客户端ack消息包之后，把之前的数据进行移除
 */
@Component
public class AckImMsgHandler implements SimpleHandler {

    private static final Logger logger = LoggerFactory.getLogger(AckImMsgHandler.class);

    @Resource
    private IMsgAckCheckService msgAckCheckService;

    @Override
    public void handler(ChannelHandlerContext ctx, ImMsg imMsg) {
        Long userId = ImContextUtils.getUserId(ctx);
        Integer appid = ImContextUtils.getAppId(ctx);
        if (userId == null && appid == null) {
            ctx.close();
            logger.error("ack handler error, {}", imMsg);
            throw new IllegalArgumentException("attr is error");
        }
        msgAckCheckService.doMsgAck(JSON.parseObject(imMsg.getBody(), ImMsgBody.class));
    }
}
