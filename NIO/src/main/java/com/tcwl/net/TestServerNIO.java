package com.tcwl.net;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class TestServerNIO {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel schannel = ServerSocketChannel.open();
        schannel.bind(new InetSocketAddress(9000));
        schannel.configureBlocking(false);

        Selector selector = Selector.open();
        schannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            System.out.println("========服务器启动=====等待连接=====");
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                //当有客户端连接的处理
                if (key.isAcceptable()) {
                    SocketChannel channel = schannel.accept();
                    channel.configureBlocking(false);
                    //准备读
                    channel.register(selector, SelectionKey.OP_READ);
                }
                //当有客户发送数据时，服务器去读取
                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(20);
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.read(buffer);
                    buffer.flip();
                    //ByteBuffer转成String
                    String str = Charset.defaultCharset().decode(buffer).toString();
                    System.out.println("服务器收到数据为：" + str);
                    str = str.toUpperCase();
                    buffer = ByteBuffer.wrap(str.getBytes());
                    channel.write(buffer);
                }
            }

        }

    }
}
