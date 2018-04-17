package com.changdy.springboot.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageReceiver {

    /**
     * 也可以自定义接受方法
     */
    public void receiveMessage(String message) {
        log.info("收到一条消息1：" + message);
    }

    /**
     * 默认情况下接收消息的方法
     */
    public void handleMessage(String message) {
        log.info("收到一条消息2：" + message);
    }
}