package com.ruoyi.im.imclient.handler;

import com.alibaba.fastjson.JSON;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.common.ImMsgDecoder;
import com.ruoyi.im.common.ImMsgEncoder;
import com.ruoyi.im.constant.AppIdEnum;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.entity.ImMsgBody;
import com.ruoyi.im.imclient.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ImClientHandler implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup clientGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(clientGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("初始化连接建立");
                        ch.pipeline().addLast(new ImMsgEncoder());
                        ch.pipeline().addLast(new ImMsgDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
                Map<Long, Channel> userIdChannelMap = new HashMap<>();
                for (int i = 0; i < 100; ++i) {
                    Long userId = (long) i;

                    ChannelFuture channelFuture = null;
                    try {
                        channelFuture = bootstrap.connect("localhost", 9094).sync();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Channel channel = channelFuture.channel();
                    ImMsgBody imMsgBody = new ImMsgBody();
                    imMsgBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                    imMsgBody.setUserId(i);
                    ImMsg loginMsg = ImMsg.build(ImMsgCodeEnum.IM_LOGIN_MSG.getCode(), JSON.toJSONString(imMsgBody));
                    channel.writeAndFlush(loginMsg);
                    userIdChannelMap.put(userId, channel);
                    /*channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_LOGOUT_MSG.getCode(), "logout"));
                    channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_BIZ_MSG.getCode(), "biz"));
                    channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_HEARTBEAT_MSG.getCode(), "heart"));*/
                }


                while(true) {
                    for (Long userId : userIdChannelMap.keySet()) {
                        ImMsgBody heartBeatBody = new ImMsgBody();
                        heartBeatBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                        heartBeatBody.setUserId(userId);
                        ImMsg heartBeatMsg = ImMsg.build(ImMsgCodeEnum.IM_HEARTBEAT_MSG.getCode(), JSON.toJSONString(heartBeatBody));
                        userIdChannelMap.get(userId).writeAndFlush(heartBeatMsg);

                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
        clientThread.start();
    }
}
