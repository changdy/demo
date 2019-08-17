package com.changdy.demo.excel.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by Changdy on 2019/8/15.
 */
public class FormatterMap {

    private static Map<Class, BiFunction<Object, Object, String>> formatterMap = new HashMap<>();
    private static BiFunction<Object, Object, String> defaultFunction = (x, y) -> x == null ? "" : x.toString();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    static {
        formatterMap.put(Date.class, (x, y) -> x == null ? "" : dateFormat.format((Date) x));
    }

    public static BiFunction<Object, Object, String> getFormatterByClass(Class c) {
        return formatterMap.getOrDefault(c, defaultFunction);
    }

    public synchronized static BiFunction<Object, Object, String> addClassFormatter(Class c, BiFunction<Object, Object, String> function) {
        return formatterMap.put(c, function);
    }
}