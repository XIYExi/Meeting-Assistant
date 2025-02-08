package com.ruoyi.im.imclient;

import com.ruoyi.im.common.ImMsg;
import com.ruoyi.im.common.ImMsgDecoder;
import com.ruoyi.im.common.ImMsgEncoder;
import com.ruoyi.im.constant.ImMsgCodeeEnum;
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

public class ImClientApplication {
    private static final Logger logger = LoggerFactory.getLogger(ImClientApplication.class);

    private void startConnection(String address, int port) throws Exception {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                logger.info("初始化连接建立");
                ch.pipeline().addLast(new ImMsgEncoder());
                ch.pipeline().addLast(new ImMsgDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
        Channel channel = channelFuture.channel();
        for (int i = 0; i < 100; ++i) {
            channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_LOGIN_MSG.getCode(), "login"));
            /*channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_LOGOUT_MSG.getCode(), "logout"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_BIZ_MSG.getCode(), "biz"));
            channel.writeAndFlush(ImMsg.build(ImMsgCodeeEnum.IM_HEARTBEAT_MSG.getCode(), "heart"));*/
            Thread.sleep(10000);
        }

    }

    public static void main(String[] args) throws Exception{
        ImClientApplication app = new ImClientApplication();
        app.startConnection("localhost", 9090);

    }
}
