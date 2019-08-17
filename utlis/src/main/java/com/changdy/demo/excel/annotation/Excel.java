package com.changdy.demo.excel.annotation;


import com.changdy.demo.excel.format.DefaultFiledFormatController;
import com.changdy.demo.excel.format.FiledFormatController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    String name();

    int ordinal() default 50;

    Class<? extends FiledFormatController> formatter() default DefaultFiledFormatController.class;
}