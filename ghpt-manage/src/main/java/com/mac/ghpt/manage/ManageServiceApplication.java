package com.mac.ghpt.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年02月28日, 16:32:21
 */
@SpringBootApplication
@MapperScan(basePackages = "com.mac.ghpt.manage.mapper")
@EnableFeignClients(basePackages = "com.mac")
//@ComponentScan(basePackages = "com.mac")
public class ManageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageServiceApplication.class, args);
    }
}
