package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@SpringBootApplication
public class DataReceiveUdpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataReceiveUdpApplication.class, args);
    }

}
