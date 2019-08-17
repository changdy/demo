package com.changdy.demo.excel.util;


import com.changdy.demo.excel.annotation.Excel;
import com.changdy.demo.excel.format.FiledFormatController;
import com.changdy.demo.excel.result.ReflectResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Created by Changdy on 2019/8/14.
 */
public class ReflectUtil {

    private static Map<Class, List<ReflectResult>> map = new HashMap<>();
    private static Map<Class, FiledFormatController> classToController = new HashMap<>();

    public synchronized static void initList(Class aClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Field[] declaredFields = aClass.getDeclaredFields();
        Map<Integer, List<ReflectResult>> treeMap = new TreeMap<>();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Excel annotation = field.getAnnotation(Excel.class);
            if (annotation != null) {
                Class<? extends FiledFormatController> formatter = annotation.formatter();
                FiledFormatController filedFormatController = classToController.get(formatter);
                if (filedFormatController == null) {
                    filedFormatController = formatter.newInstance();
                    classToController.put(formatter, filedFormatController);
                }
                Method getFormatter = formatter.getMethod("getFormatter", Class.class, Field.class);
                ReflectResult reflectResult = new ReflectResult(field, annotation.name());
                reflectResult.setFunction((BiFunction) getFormatter.invoke(filedFormatController, aClass, field));
                int ordinal = annotation.ordinal();
                List<ReflectResult> list = treeMap.getOrDefault(ordinal, new ArrayList<>());
                list.add(reflectResult);
                treeMap.put(ordinal, list);
            }
        }
        List<ReflectResult> results = new ArrayList<>();
        treeMap.forEach((x, y) -> results.addAll(y));
        map.put(aClass, results);
    }

    public static List<String> getTitleList(Object o) {
        if (!map.keySet().contains(o.getClass())) {
            try {
                initList(o.getClass());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        List<ReflectResult> list = map.get(o.getClass());
        List<String> s = new ArrayList<>();
        for (ReflectResult reflectResult : list) {
            s.add(reflectResult.getName());
        }
        return s;
    }


    public static String getTitleString(Object o, String delimiter) {
        return String.join(delimiter, getTitleList(o));
    }

    public static List<String> getBodyList(Object source) {
        List<ReflectResult> list = map.get(source.getClass());
        List<String> s = new ArrayList<>();
        try {
            for (ReflectResult reflectResult : list) {
                Object fieldValue = reflectResult.getField().get(source);
                BiFunction<Object, Object, String> function = reflectResult.getFunction();
                s.add(function.apply(fieldValue, source));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return s;
    }

    public static String getBodyString(Object o, String delimiter) {
        return String.join(delimiter, getBodyList(o));
    }
}