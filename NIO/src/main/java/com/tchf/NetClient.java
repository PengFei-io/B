package com.tchf;

import javafx.scene.transform.Scale;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 客户端连接
 */
public class NetClient {
    static SocketChannel channel;

    public static void main(String[] args) throws Exception {
        channel = SocketChannel.open();
        //设置非阻塞
        channel.configureBlocking(false);
        //设置连接端口号
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);
        //连接服务器
        if (!channel.connect(address)) {
            while (true) {//不断尝试连接
                boolean b = channel.finishConnect();
                if(b==true){break;}
                System.out.println("==正在尝试连接==");
            }
        }
        //得到客户端ip，作为聊天用户名使用
        String name = channel.getLocalAddress().toString();
        System.out.println("客户端:" + name + "已经准备好");
        //向服务器发送数据
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("客户端请输入数据");
            if (scanner.next().equalsIgnoreCase("bye")) {
                channel.close();
                return;
            }
            ByteBuffer buffer = ByteBuffer.wrap(scanner.next().getBytes());
            channel.write(buffer);
            accept();
        }

    }

    /**
     * 客户端发送数据
     *
     * @param msg
     */
    static void sendMsg(String msg) {
        try {
            if (msg.equalsIgnoreCase("bye")) {
                channel.close();
                return;
            }
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            channel.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器接收数据
     */
    static void accept() {
        try {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            if (read > 0) {
                String msg = new String(buffer.array());
                System.out.println("客户端接收到数据：" + msg.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
