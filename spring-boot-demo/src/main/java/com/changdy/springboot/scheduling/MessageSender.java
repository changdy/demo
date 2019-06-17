package com.changdy.springboot.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MessageSender {

    @Scheduled(fixedRate = 2000)
    public void sendRedisMessage() {
        log.info("hello");
    }
}
