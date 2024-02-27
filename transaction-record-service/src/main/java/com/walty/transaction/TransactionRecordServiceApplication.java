package com.walty.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TransactionRecordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionRecordServiceApplication.class, args);
    }
}
