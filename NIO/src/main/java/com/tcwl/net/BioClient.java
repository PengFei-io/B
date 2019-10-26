package com.tcwl.net;

import java.io.*;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 9000);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        PrintWriter out = new PrintWriter(os);
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(ir);
        for (int i = 1; i <= 30; i++) {
            out.println("hello" + i);
            out.flush();
            String str = in.readLine();
            System.out.println(str);
            Thread.sleep(200);
        }
//        socket.close();
    }
}
