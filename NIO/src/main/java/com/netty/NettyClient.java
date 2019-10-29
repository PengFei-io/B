package com.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {
        //1.创建一个线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2.创建客户端的启动助手，完成相关配置
        Bootstrap b = new Bootstrap();
        //3.设置线程组
        b.group(group)
                //配置客户端通道的实现类
                .channel(NioSocketChannel.class)
                //创建通道初始化对象
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //在Pipeline中加入自定义客户端事件
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("客户端准备就绪。。。。。。。。");
        //启动客户端，连接服务器
        ChannelFuture future = b.connect("localhost", 9898).sync();
        //关闭连接
        future.channel().closeFuture().sync();
    }
}
