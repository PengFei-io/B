package com;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScans(
        @MapperScan(basePackages = "com.io.dao")
)
public class UserModelApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserModelApplication.class, args);
    }

}

