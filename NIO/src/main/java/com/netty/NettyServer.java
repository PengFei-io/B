package com.netty;

import com.code.BookMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务器
 */
@Component
public class NettyServer {
    @Autowired
    NettyServerHandler nettyServerHandler;
    ChannelFuture cf = null;
    NioEventLoopGroup bossGroup = null;
    NioEventLoopGroup workGroup = null;
    ServerBootstrap serverBootstrap = null;

    public void start(int port) {
        //1.创建一个线程组,接收客户端连接
        bossGroup = new NioEventLoopGroup();
        //2.创建一个线程组，用于处理网络操作
        workGroup = new NioEventLoopGroup();
        //3.创建服务器端的启动助手，配置参数
        serverBootstrap = new ServerBootstrap();
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
                        sc.pipeline().addLast("decoder", new ProtobufDecoder(BookMessage.Book.getDefaultInstance()));
                        sc.pipeline().addLast(nettyServerHandler);
                    }
                });

        try {
            cf = serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("...........Server is starting........");
    }

    public void close() {
        //关闭通道，关闭线程组
        try {
            if (cf != null) {
                cf.channel().closeFuture().sync();//异步
            }
            if (bossGroup != null) {

                bossGroup.shutdownGracefully();
            }
            if (workGroup != null) {
                workGroup.shutdownGracefully();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
