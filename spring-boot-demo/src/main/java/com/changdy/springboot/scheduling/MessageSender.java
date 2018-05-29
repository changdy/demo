package com.changdy.springboot.scheduling;

import com.changdy.springboot.bean.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.integration.support.MessageBuilder;

import java.time.LocalTime;


@Component
@Slf4j
public class MessageSender {

    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageChannel kafkaTopicChannel;

    @Scheduled(fixedRate = 2000)
    public void sendRedisMessage() {
        stringRedisTemplate.convertAndSend(redisConfig.getChannel(), String.valueOf(Math.random()));
    }

    @Scheduled(fixedRate = 2000)
    public void sendKafkaMessage() {
        String topicClient = "wshuttle-client";
        String topicWsccc = "wshuttle-wsccc";
        boolean send;
        send = kafkaTopicChannel.send(MessageBuilder.withPayload(topicClient + LocalTime.now()).setHeader(KafkaHeaders.TOPIC, topicClient).build());
        log.info(topicClient + ": {}", send);
        send = kafkaTopicChannel.send(MessageBuilder.withPayload(topicWsccc + LocalTime.now()).setHeader(KafkaHeaders.TOPIC, topicWsccc).build());
        log.info(topicWsccc + ":{}", send);
    }

}
