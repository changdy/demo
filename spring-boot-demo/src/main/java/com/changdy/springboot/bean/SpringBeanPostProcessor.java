package com.changdy.springboot.bean;

import com.changdy.springboot.introspector.CustomJacksonAnnotationIntrospector;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Slf4j
public class SpringBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MappingJackson2HttpMessageConverter) {
            SimpleModule module = new SimpleModule();
            module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeNumber(Timestamp.valueOf(value).getTime());
                }
            });
            module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                    Instant instant = Instant.ofEpochMilli(p.getLongValue());
                    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                }
            });
            ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) bean).getObjectMapper();
            objectMapper.registerModule(module);
            objectMapper.setAnnotationIntrospectors(new CustomJacksonAnnotationIntrospector(), new JacksonAnnotationIntrospector());
        }
        return bean;
    }
}