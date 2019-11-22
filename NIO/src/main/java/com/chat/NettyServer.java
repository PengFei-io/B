package com.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;

/**
 * 服务器
 */
public class NettyServer {
    private static final Integer port = 9898;
    static NioEventLoopGroup bossGroup = null;
    static NioEventLoopGroup workGroup = null;
    static ServerBootstrap serverBootstrap = null;
    static ChannelFuture cf = null;

    static {
        //1.创建一个线程组,接收客户端连接
        bossGroup = new NioEventLoopGroup();
        //2.创建一个线程组，用于处理网络操作
        workGroup = new NioEventLoopGroup();
        //3.创建服务器端的启动助手，配置参数
        serverBootstrap = new ServerBootstrap();

    }

    public static void start() {
        try {
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
                            ChannelPipeline pipeline = sc.pipeline();
                            //在pipeline链中加入解码器
                            pipeline.addLast("decoder", new StringDecoder(Charset.forName("GBK")));
                            //在pipeline链中加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //在pipeline链中加入自定义业务处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("...........Server is starting........");
            //bind 是异步的,会立即返回future对象
            cf = serverBootstrap.bind(port).sync();
            cf.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future future) throws Exception {
                    System.out.println("监听启动");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭通道，关闭线程组
            try {
                if (cf != null) {
                    cf.channel().closeFuture().sync();//异步
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workGroup != null) {
                workGroup.shutdownGracefully();
            }
            System.out.println("...........Server is close........");
        }
    }
}
