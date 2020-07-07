package com.changdy.springboot.controller;

import com.changdy.springboot.model.TestBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    // @Validated 与 @Valid 区别 以及一些分组的高级功能
    // https://blog.csdn.net/littleskey/article/details/52224352
    @RequestMapping("/test-Validated")
    public String throwValidated(@RequestBody @Validated TestBody testBody) throws Exception {
        throw new Exception();
    }

    // 可以使用params 达到版本控制
    @RequestMapping(value = "/test-Validated", params = {"apiVersion=0.2.0"})
    public String throwValidatedNewVersion(@RequestBody @Validated TestBody testBody) throws Exception {
        throw new Exception();
    }
}
