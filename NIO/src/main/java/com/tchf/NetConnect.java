package com.tchf;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 服务器端socket
 */
public class NetConnect {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //获得selector
            Selector selector = Selector.open();
            //设置端口号
            serverSocketChannel.bind(new InetSocketAddress(9999));
            //设置非阻塞方式
            serverSocketChannel.configureBlocking(false);
            //注册服务器到Selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //服务器接收信息
            while (true) {
                //没有客户端连接
                if (selector.select(5000) == 0) {
                    System.out.println("server:=====暂时没有客户端连接====");
                    continue;
                }
                //得到连接的SelectionKey,判断通道内的事件
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {//客户端连接事件
                        System.out.println("==========客户端已经连接=======");
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        //设置非阻塞
                        socketChannel.configureBlocking(false);
                        //注册到Selector
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    }
                    if (key.isReadable()) {//读取客户端数据事件
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        channel.read(buffer);
                        buffer.clear();
                        System.out.println("客户端发来数据:" + new String(buffer.array()));
                    }
                    //手动从集合中移除当前key，防止重复处理
                    keyIterator.remove();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
