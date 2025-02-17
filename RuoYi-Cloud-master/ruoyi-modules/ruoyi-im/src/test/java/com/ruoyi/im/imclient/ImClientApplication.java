package com.ruoyi.im.imclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.common.ImMsgDecoder;
import com.ruoyi.im.common.ImMsgEncoder;
import com.ruoyi.im.constant.AppIdEnum;
import com.ruoyi.im.constant.ImMsgCodeEnum;
import com.ruoyi.im.entity.ImMsgBody;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class ImClientApplication {
    private static final Logger logger = LoggerFactory.getLogger(ImClientApplication.class);

    private void startConnection(String address, int port) throws Exception {
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
                        channelFuture = bootstrap.connect(address, port).sync();
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
                        System.err.println("发送biz message...");
                        ImMsgBody bizBody = new ImMsgBody();
                        bizBody.setAppId(AppIdEnum.LIVE_BIZ.getCode());
                        bizBody.setUserId(userId);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userId", userId);
                        jsonObject.put("objectId", 1001101L);
                        jsonObject.put("content", "你好，我是" +userId);
                        bizBody.setData(JSON.toJSONString(jsonObject));
                        ImMsg heartBeatMsg = ImMsg.build(ImMsgCodeEnum.IM_BIZ_MSG.getCode(), JSON.toJSONString(bizBody));
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

    public static void main(String[] args) throws Exception{
        ImClientApplication app = new ImClientApplication();
        app.startConnection("localhost", 9094);
    }
}
