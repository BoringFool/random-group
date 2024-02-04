package com.zm.user.business.service;

import com.zm.user.business.util.RabbitMQUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitMQUtils rabbitMQUtils;

    public String send() {
        try {
            rabbitMQUtils.sendMsg("test");
            Thread.sleep(80000);
        } catch (AmqpException e) {
            throw new AmqpException("Failed to send message to RabbitMQ", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "ok";
    }
}
