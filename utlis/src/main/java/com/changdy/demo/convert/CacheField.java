package com.changdy.demo.convert;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheField {
    String value();

    // 被转换后的值存放的位置 默认原属性名称+"Name"
    String destination() default "";
}