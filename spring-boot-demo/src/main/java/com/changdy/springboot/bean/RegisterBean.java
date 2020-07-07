package com.changdy.springboot.bean;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by Changdy on 2018/4/14.
 */
@Slf4j
@Configuration
public class RegisterBean {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @PostConstruct
    public void changeBean() {
        threadPoolTaskScheduler.setErrorHandler(x -> log.error("定时任务异常", x));
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(5000);
        return loggingFilter;
    }

    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("async");
        // 以mdc为例子传递上下文 ,还可以传递 Spring Security 之类的
        executor.setTaskDecorator(new MdcTaskDecorator());
        return executor;
    }

    public static class MdcTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }


    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurer() {
            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return (ex, method, params) -> {
                    // 更正确的是应该把这次 异常信息 抛给消息队列,让消息队列入栈
                    //exceptionMapper.insertException(ex.getClass().toString(), ex.toString(), Arrays.toString(ex.getStackTrace()), "APP", "异步线程异常");
                    log.error("异常", ex);
                    log.info("方法名{}", method);
                    log.info("参数{}", params);
                };
            }
        };
    }


     //跨域
    //@Bean
    //public CorsFilter corsFilter() {
    //    CorsConfiguration corsConfiguration = new CorsConfiguration();
    //    corsConfiguration.addAllowedOrigin("*");
    //    corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
    //    corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
    //    corsConfiguration.setAllowCredentials(true);
    //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //    source.registerCorsConfiguration("/callback/oss", corsConfiguration); // 4 对接口配置跨域设置
    //    return new CorsFilter(source);
    //}
}
