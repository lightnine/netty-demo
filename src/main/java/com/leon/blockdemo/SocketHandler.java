package com.leon.blockdemo;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ClassName SocketHandler
 * Description:
 * Create by leon
 * Date 2020/7/9 22:23
 */
public class SocketHandler implements Runnable {
    private SocketChannel socketChannel;

    public SocketHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int num;
            // channel 数据读取到 buffer中
            while ((num = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                byte[] bytes = new byte[num];
                buffer.get(bytes);
                String re = new String(bytes, "UTF-8");
                System.out.println("收到的请求：" + re);
                ByteBuffer writeBuffer = ByteBuffer.wrap(("我已经收到你的请求，你的请求内容是：" + re).getBytes());
                // buffer内容写到channel中
                socketChannel.write(writeBuffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(socketChannel);
        }
    }
}
