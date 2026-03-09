package com.chursin.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {
                "com.chursin.customer",
                "com.chursin.amqp"
        }
)
@EnableDiscoveryClient
@EnableFeignClients(
    basePackages = {
            "com.chursin.client",
            "com.chursin.*"
    }
)
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
