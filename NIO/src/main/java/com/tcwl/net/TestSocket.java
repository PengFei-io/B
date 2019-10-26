package com.tcwl.net;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TestSocket {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(9000);
        while (true) {
            InetAddress inetAddress = ss.getInetAddress();
            System.out.println("================启动监听=====================");
            Socket s = ss.accept();
            InetAddress inetAddress1 = s.getInetAddress();
            System.out.println("地址1是：" + inetAddress1);

            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        InputStream is = s.getInputStream();
                        OutputStream os = s.getOutputStream();
                        PrintWriter out = new PrintWriter(os);
                        InputStreamReader ir = new InputStreamReader(is);
                        BufferedReader in = new BufferedReader(ir);
                        for (int i = 1; i <= 30; i++) {
                            String str2 = in.readLine().toUpperCase();
                            out.println(str2);
                            System.out.println("客户端收到数据:" + str2);
                            out.flush();
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            s.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            };
            thread.start();
        }
//        ss.close();
    }
}
