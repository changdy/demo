package com.changdy.demo.excel.format;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * Created by Changdy on 2019/8/14.
 */
public class DefaultFiledFormatController implements FiledFormatController {
    @Override
    public BiFunction<Object, Object, String> getFormatter(Class sourceClass, Field field) {
        return FormatterMap.getFormatterByClass(field.getType());
    }
}