package com.zm.user.business;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDubbo
@EnableRabbit
@MapperScan("com.zm.user.business.mapper")
@EnableTransactionManagement
public class RandomUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(RandomUserApplication.class, args);
    }
}
