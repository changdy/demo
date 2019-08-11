package com.changdy.springboot.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by Changdy on 2017/11/29.
 */
@Slf4j
@Aspect
@Component
// 考虑把这个功能放到kong 上面
public class HttpAspect {
    @Autowired
    private ObjectMapper o;

    @Pointcut("execution(public * com.changdy.springboot.controller.*.*(..))")
    public void log() {
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) throws JsonProcessingException {
        log.info("response={}", o.writeValueAsString(object));
    }
}
