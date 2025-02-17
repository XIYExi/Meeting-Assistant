package com.ruoyi.im.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.im.common.ChannelHandlerContextCache;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.entity.ImMsgBody;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;


@Service
public class RouterHandlerServiceImpl implements IRouterHandlerService{

    @Override
    public void onReceive(ImMsgBody imMsgBody) {
        long userId = imMsgBody.getUserId();
        ChannelHandlerContext ctx = ChannelHandlerContextCache.get(userId);
        if (ctx!= null) {
            ImMsg respMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(imMsgBody));
            ctx.writeAndFlush(respMsg);
        }
    }
}
