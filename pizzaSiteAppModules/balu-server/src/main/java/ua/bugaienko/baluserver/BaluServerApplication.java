package ua.bugaienko.baluserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class BaluServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaluServerApplication.class, args);
    }

}
