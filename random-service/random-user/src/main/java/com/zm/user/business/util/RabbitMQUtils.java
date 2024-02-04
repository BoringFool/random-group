package com.zm.user.business.util;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQUtils {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQUtils(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMsg(String msg) throws AmqpException {
        try {
            rabbitTemplate.convertAndSend("test.directExchange", "test.directRouting", msg);
        } catch (AmqpException e) {
            throw new AmqpException ("RabbitMQ发送消息异常", e);
        }
    }
}
