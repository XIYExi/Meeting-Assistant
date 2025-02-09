package com.ruoyi.im.starter;

import com.ruoyi.im.EnvelopeNettyImServerApplication;
import com.ruoyi.im.common.ImMsgDecoder;
import com.ruoyi.im.common.ImMsgEncoder;
import com.ruoyi.im.handler.ImServerCoreHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
public class NettyServerStarter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EnvelopeNettyImServerApplication.class);

    // 指定监听端口
    @Value("${netty.port}")
    private int port;

    @Resource
    private ImServerCoreHandler imServerCoreHandler;

    // 基于netty启动一个java进程，绑定监听窗口
    public void startApplication() throws InterruptedException {
        //处理read事件 【其实就是一个线程池】
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();;
        //处理read&write事件 【+1】
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();;
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        // netty初始化相关handler
        bootstrap.childHandler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                logger.info("初始化连接渠道");
                // 定义编解码器
                ch.pipeline().addLast(new ImMsgDecoder());
                ch.pipeline().addLast(new ImMsgEncoder());
                ch.pipeline().addLast(imServerCoreHandler);
            }
        });

        // 基于jvm hook实现优雅shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }));

        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        logger.info("Netty服务启动成功，监听 {} 端口", port);
        // 阻塞主线程，实现服务长期开启的效果
        channelFuture.channel().closeFuture().sync();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Thread nettyServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startApplication();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finally {

                }
            }
        });
        nettyServerThread.setName("envelope-live-in-server");
        nettyServerThread.start();
    }
}
