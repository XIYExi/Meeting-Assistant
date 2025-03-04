package com.ruoyi.rag.tcp.server;

import com.ruoyi.rag.tcp.db.DeviceCtxDb;
import com.ruoyi.rag.tcp.domain.TcpQueue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;
import java.util.stream.Collectors;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 处理消息
        System.out.println("Received message: " + msg.text());
        // ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + msg.text()));
        List<TcpQueue> tmplst =  DeviceCtxDb.queueList.stream().filter(e->
                e.getChannelId().equals(ctx.channel().id().toString())
        ).collect(Collectors.toList());
        if(tmplst.size()>0){
            //说明已经注册过了,是一般操作指令
            //把这个更新到数据库
            // ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + msg.text()));
        }else {
            TcpQueue queue=new TcpQueue();
            queue.setChannelId(ctx.channel().id().toString());
            queue.setChannelHandlerContext(ctx);
            queue.setUId(msg.text());
            DeviceCtxDb.queueList.add(queue);
        }
    }

    public void sendMsg( ChannelHandlerContext ctx, String msg) throws Exception {

        if(msg.contains("&")){
            //是系统发的指令,第一个是设备id，第二个是命令
            String [] mlst=msg.split("&");
            for (int i = 0; i < DeviceCtxDb.queueList.size(); i++) {
                if(DeviceCtxDb.queueList.get(i).getUId().contains(mlst[0])){
                    if(mlst.length>1){
                        ChannelHandlerContext ctxobj=DeviceCtxDb.queueList.get(i).getChannelHandlerContext();
                        ctxobj.channel().writeAndFlush(new TextWebSocketFrame(mlst[1]));
                    }

                }
            }
        } else {
            ctx.write((new TextWebSocketFrame(msg)));
            ctx.flush();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加连接
        System.out.println("Client connected: " + ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 断开连接
        for (int i = 0; i <  DeviceCtxDb.queueList.size(); i++) {
            if( DeviceCtxDb.queueList.get(i).getChannelId().equals(ctx.channel().id().toString())){
                DeviceCtxDb.queueList.remove(DeviceCtxDb.queueList.get(i));
            }
        }
        System.out.println("Client disconnected: " + ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
