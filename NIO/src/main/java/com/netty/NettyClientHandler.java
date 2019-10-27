package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * 客户端的业务处理类
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client:" + ctx);
            String s = "请您输入。。。。。。。。。。。";
            ctx.writeAndFlush(Unpooled.copiedBuffer(s, Charset.forName("GBK")));
    }

    //读取服务器发来的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器发来消息：" + buf.toString(Charset.forName("GBK")));
    }
}
