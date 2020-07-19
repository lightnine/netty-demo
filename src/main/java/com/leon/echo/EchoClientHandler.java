package com.leon.echo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ClassName EchoClientHandler
 * Description:
 * Create by leon
 * Date 2020/7/11 11:19
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf firstMessage;

    public EchoClientHandler() {
        this.firstMessage = Unpooled.buffer(EchoClient.SIZE);

        for(int i = 0; i < this.firstMessage.capacity(); ++i) {
            this.firstMessage.writeByte((byte)i);
        }

    }

    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(this.firstMessage);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ctx.write(msg);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
