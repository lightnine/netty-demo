package com.leon.aiodemo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * ClassName Server
 * Description: 采用AIO的server来实现服务端
 * Create by leon
 * Date 2020/7/10 22:47
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 创建server, 绑定到8080端口
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(8080));
        Attachment att = new Attachment();
        att.setServer(server);
        // server 接受连接 , 参数2是用来消费收到的连接
        server.accept(att, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {
            @Override
            public void completed(AsynchronousSocketChannel client, Attachment attachment) {
                try {
                    SocketAddress clientAddr = client.getRemoteAddress();
                    System.out.println("收到的新连接:" + clientAddr);
                    att.getServer().accept(att, this);

                    Attachment newAtt = new Attachment();
                    newAtt.setServer(server);
                    newAtt.setClient(client);
                    newAtt.setReadMode(true);
                    newAtt.setBuffer(ByteBuffer.allocate(1024));
                    client.read(newAtt.getBuffer(), newAtt, new ChannelHandler());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Attachment attachment) {
                System.out.println("accept failed");
            }
        });
        try {
            // 让主线程一直等待
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
