package com.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        //1.创建一个线程组,接收客户端连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.创建一个线程组，用于处理网络操作
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        //3.创建服务器端的启动助手，配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置2个线程组
        serverBootstrap.group(bossGroup, workGroup)
                //使用NioServerSocketChannel作为服务器端通道的实现
                .channel(NioServerSocketChannel.class)
                //设置线程队列中等待连接的个数
                .option(ChannelOption.SO_BACKLOG, 123)
                //保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //创建一个通道初始化对象
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //往Pipeline链中添加自定义的handler类
                    @Override
                    public void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new NettyServerHandler());
                    }
                });
        ChannelFuture cf = serverBootstrap.bind(9898).sync();
        System.out.println("...........Server is starting........");
        //关闭通道，关闭线程组
        cf.channel().closeFuture().sync();//异步
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();

    }
}
