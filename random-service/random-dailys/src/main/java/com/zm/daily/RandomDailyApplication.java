package com.zm.daily;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDubbo
@EnableRabbit
@MapperScan("com.zm.daily.mapper")
@EnableTransactionManagement
public class RandomDailyApplication {
    public static void main(String[] args) {
        SpringApplication.run(RandomDailyApplication.class, args);
    }
}
