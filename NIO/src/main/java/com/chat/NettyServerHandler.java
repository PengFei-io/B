package com.chat;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器端的业务处理类
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    static List<Channel> channels = new ArrayList<>();

    //客户端就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println("{Server:}" + channel.remoteAddress().toString().substring(1) + "上线！！！");
    }

    //客户端未就绪事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println("{Server:}" + channel.remoteAddress().toString().substring(1) + "下线！！！");
    }

    //读取数据事件
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        for (Channel channel1 : channels) {
            if (channel1 != channel) {
                channel1.writeAndFlush("[" + channel.remoteAddress().toString().substring(1) + "]" + "说" + msg + "\n");
            }
        }
        System.out.println("接收到客户端信息："+msg);
    }

    /**
     * 数据读取完毕事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       // ctx.writeAndFlush(Unpooled.copiedBuffer("公司倒闭 没钱了", Charset.forName("GBK")));
    }


    /**
     * 异常发生事件
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务器端发生异常");
        ctx.close();
    }
}
