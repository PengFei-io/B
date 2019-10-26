package com.tcwl.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class TestClientNIO {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        channel.connect(new InetSocketAddress("localhost", 9000));

        Thread t = new Thread() {
            @Override
            public void run() {
                while (!channel.isConnected()) {
                }
                try {
                    for (int i = 1; i <= 30; i++) {
                        String str = "hello" + i;
                        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
                        channel.write(buffer);
                        Thread.sleep(200);
                    }
                    channel.close();
                    selector.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

        while (true) {
            selector.select();
            if (!selector.isOpen()) break;
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isConnectable()) {
                    if (!channel.isConnected()) {
                        //如果连接还没完成，则完成连接
                        channel.finishConnect();
                    }
                    channel.register(selector, SelectionKey.OP_READ);

                }
                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(20);
                    channel.read(buffer);
                    buffer.flip();
                    String str = Charset.defaultCharset().decode(buffer).toString();
                    System.out.println(str);
                }
            }

        }
    }
}
