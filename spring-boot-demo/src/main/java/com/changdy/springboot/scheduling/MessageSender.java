package com.changdy.springboot.scheduling;

import com.changdy.springboot.bean.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.integration.support.MessageBuilder;

import java.time.LocalDateTime;


@Component
public class MessageSender {

    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageChannel inputToKafka;

    @Scheduled(fixedRate = 2000)
    public void sendRedisMessage() {
        stringRedisTemplate.convertAndSend(redisConfig.getChannel(), String.valueOf(Math.random()));
    }

    @Scheduled(fixedRate = 2000)
    public void sendKafkaMessage() {
        inputToKafka.send(MessageBuilder.withPayload("Message-" + LocalDateTime.now()).build());
    }

}
