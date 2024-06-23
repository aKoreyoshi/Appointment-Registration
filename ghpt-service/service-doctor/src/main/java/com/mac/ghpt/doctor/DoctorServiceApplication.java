package com.mac.ghpt.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.mac"})
@ComponentScan("com.mac")
public class DoctorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorServiceApplication.class, args);
    }
}