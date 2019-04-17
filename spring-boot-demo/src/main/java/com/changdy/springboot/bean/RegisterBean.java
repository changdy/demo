package com.changdy.springboot.bean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Changdy on 2018/4/14.
 */
@Configuration
public class RegisterBean {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private TopicMessageListener messageListener;
    @Autowired
    private MessageReceiver receiver;
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


    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic(redisConfig.getChannel()));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * <p>
     * 这个必须注册到bean里面,否则会出错
     */
    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        //这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        //也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage 可以自己到源码里面看
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}