package com.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 客户端
 */
public class NettyClient {
    static final String ipAddres = "localhost";
    static final Integer port = 9898;
    static NioEventLoopGroup group = null;
    static Bootstrap b = null;

    static {
        //1.创建一个线程组
        group = new NioEventLoopGroup();
        //2.创建客户端的启动助手，完成相关配置
        b = new Bootstrap();
    }

    public static void start() {
        //3.设置线程组
        b.group(group)
                //配置客户端通道的实现类
                .channel(NioSocketChannel.class)
                //创建通道初始化对象
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        //在pipeline链中加入解码器
                        pipeline.addLast("decoder", new StringDecoder());
                        //在pipeline链中加入编码器
                        pipeline.addLast("encoder", new StringEncoder());
                        //在pipeline链中加入自定义业务处理器
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
        System.out.println("客户端准备就绪。。。。。。。。");
        //启动客户端，连接服务器
        ChannelFuture future = null;
        try {
            //sync方法是阻塞方法
            future = b.connect(ipAddres, port).sync();
            Channel channel = future.channel();
            System.out.println("----" + channel.localAddress().toString().substring(1) + "----");
//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNextLine()) {
//                System.out.println("请输入聊天内容");
//                String msg = scanner.nextLine();
//                channel.writeAndFlush(msg + "\r\n");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            try {
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
