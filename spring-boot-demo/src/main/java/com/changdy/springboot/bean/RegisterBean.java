package com.changdy.springboot.bean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

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
    private TopicMessageListener messageListener;
    @Autowired
    private RedisConnectionFactory connectionFactory;

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
        return executor;
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


    @Bean
    public RedisMessageListenerContainer configRedisMessageListenerContainer() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(redisConfig.getCorePoolSize());
        executor.setMaxPoolSize(redisConfig.getMaxPoolSize());
        executor.setQueueCapacity(redisConfig.getQueueCapacity());
        executor.setKeepAliveSeconds(redisConfig.getKeepAliveSeconds());
        executor.setThreadNamePrefix(redisConfig.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        // 设置监听使用的线程池
        container.setTaskExecutor(executor);
        // 设置监听的Topic
        ChannelTopic channelTopic = new ChannelTopic(redisConfig.getExpiredTopic());
        // 设置监听器
        container.addMessageListener(messageListener, channelTopic);
        return container;
    }

    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        JsonSerializer<LocalDateTime> localDateTimeJsonSerializer = new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(Timestamp.valueOf(value).getTime());
            }
        };
        JsonDeserializer<LocalDateTime> localDateTimeJsonDeserializer = new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                Instant instant = Instant.ofEpochMilli(p.getLongValue());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
        };
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializerByType(LocalDateTime.class, localDateTimeJsonSerializer)
                .deserializerByType(LocalDateTime.class, localDateTimeJsonDeserializer);
    }


    // 跨域
    //@Bean
    //public CorsFilter corsFilter() {
    //    CorsConfiguration corsConfiguration = new CorsConfiguration();
    //    corsConfiguration.addAllowedOrigin("*");
    //    corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
    //    corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
    //    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //    source.registerCorsConfiguration("/callback/oss", corsConfiguration); // 4 对接口配置跨域设置
    //    return new CorsFilter(source);
    //}
}