package com.ruoyi.im.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NioSimpleServer {
    private static List<SocketChannel> acceptSocketList = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress((9090)));
        serverSocketChannel.configureBlocking(false);

        new Thread(() -> {
            while(true) {
                for (SocketChannel socketChannel : acceptSocketList) {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        socketChannel.read(byteBuffer);
                        System.out.println("服务器接收到的数据为： " + new String(byteBuffer.array()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while(true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                System.out.println("连接建立");
                socketChannel.configureBlocking(false);
                acceptSocketList.add(socketChannel);
            }
        }

    }
}
