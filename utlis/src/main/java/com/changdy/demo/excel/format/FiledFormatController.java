package com.changdy.demo.excel.format;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * Created by Changdy on 2019/8/14.
 */
public interface FiledFormatController {

    // BiFunction 第一个参数为field值,第二个参数为原对象值,String为返回值
    BiFunction<Object, Object, String> getFormatter(Class sourceClass, Field field);
}