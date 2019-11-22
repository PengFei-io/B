package com;

import com.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RunJob implements CommandLineRunner {
    @Autowired
    NettyServer nettyServer;
    @Value("${netty.port}")
    private Integer port;

    public static void main(String[] args) {
        SpringApplication.run(RunJob.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServer.start(port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                nettyServer.close();
            }
        });
    }


}
