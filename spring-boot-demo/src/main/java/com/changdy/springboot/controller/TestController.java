package com.changdy.springboot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Changdy on 2018/5/29.
 */
@RestController
public class TestController {
    @PostMapping("/post-only")
    public Map postOnly() {
        Map jsonObject = new HashMap();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("long", Long.MAX_VALUE / 15);
        jsonObject.put("int", Integer.MAX_VALUE);
        return jsonObject;
    }

    @RequestMapping("/throw-exception")
    public String throwException() throws Exception {
        throw new Exception();
    }
}
