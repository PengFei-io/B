package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
// java -jar xxxx.jar --spring.profiles.active=eureka-1
public class EurekaRegistryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaRegistryServerApplication.class, args);
    }
}
