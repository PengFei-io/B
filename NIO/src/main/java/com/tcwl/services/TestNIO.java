package com.tcwl.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestNIO {
    // @Test
    public void method01() throws Exception {
        long millis1 = System.currentTimeMillis();
        //读进来
        FileInputStream inputStream = new FileInputStream("D:\\IdeaWorkSpace\\B\\NIO\\src\\main\\java\\com\\tcwl\\services\\01_IO流的概念和分类.wmv");
        FileChannel channel = inputStream.getChannel();
        ByteBuffer inBuffer = ByteBuffer.allocate(1024);

        //写出去
        FileOutputStream outputStream = new FileOutputStream("D:\\IdeaWorkSpace\\B\\NIO\\src\\main\\java\\com\\tcwl\\services\\02_IO流的概念和分类.wmv");
        FileChannel channel2 = outputStream.getChannel();
        channel.transferTo(0, channel.size(), channel2);
        channel.close();
        channel2.close();
        long millis2 = System.currentTimeMillis();
        System.out.println((millis2 - millis1));

    }
}
