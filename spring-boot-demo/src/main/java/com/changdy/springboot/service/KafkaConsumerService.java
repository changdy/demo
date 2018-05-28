package com.changdy.springboot.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class KafkaConsumerService {
    public void processMessage(Map<String, Map<Integer, List<String>>> msg) {
        msg.forEach((topic, j) -> j.forEach((k, l) -> {
            log.info("本次接受长度{}", l.size());
        }));
    }
}
