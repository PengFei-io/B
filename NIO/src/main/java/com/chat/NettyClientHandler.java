package com.chat;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端的业务处理类
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

    /*
        //通道未就绪事件
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
         //   channels.remove(channel);
            System.out.println("{Server:}" + channel.remoteAddress().toString().substring(1) + "下线！！！");
        }
*/
    //通道就绪事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("运行到客户端通道就绪事件了");
    }

    //读取数据事件
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String buf) throws Exception {

        System.out.println("服务器发来消息：" + buf);
    }

}
