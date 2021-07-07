package com.changdy.demo.convert;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ConvertUtil {
    private static final Map<Class<?>, List<CacheHandler>> CLASS_HANDLER_MAP = new HashMap<>();

    private static final ThreadLocal<HashSet> HAS_SET = new ThreadLocal<>();

    public static <T> T convert(T o) {
        HAS_SET.set(new HashSet());
        try {
            convertByType(o);
        } catch (Exception e) {
            log.error("转义异常", e);
        }
        HAS_SET.remove();
        return o;
    }


    private static <T> void convertByType(T o) throws IllegalAccessException, NoSuchFieldException {
        if (o == null) {
            return;
        }
        if (o instanceof ConvertedAble) {
            if (!HAS_SET.get().contains(o)) {
                HAS_SET.get().add(o);
                // 这里其实也可以 加锁避免多线程下多次被转换, 但想了下冲突概率比较少,并且本身幂等.遂放弃
                solveCacheByHandler(o, CLASS_HANDLER_MAP.getOrDefault(o.getClass(), reflectEntity(o.getClass(), null)));
                HAS_SET.get().remove(o);
            }
        } else if (o.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(o); i++) {
                convertByType(Array.get(o, i));
            }
        } else if (o instanceof Iterable) {
            for (Object entity : (Iterable<?>) o) {
                convertByType(entity);
            }
        } else if (o instanceof Map) {
            ((Map<?, ?>) o).forEach((x, y) -> {
                try {
                    convertByType(x);
                    convertByType(y);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static List<CacheHandler> reflectEntity(Class<?> entityClass, List<CacheHandler> list) throws NoSuchFieldException {
        if (list == null) {
            list = new ArrayList<>();
            CLASS_HANDLER_MAP.put(entityClass, list);
        }
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field originalField : declaredFields) {
            originalField.setAccessible(true);
            if (originalField.isAnnotationPresent(com.example.test.CacheField.class)) {
                Class<?> type = originalField.getType();
                if (type == Integer.class || type == Byte.class || type == String.class) {
                    com.example.test.CacheField annotation = originalField.getAnnotation(com.example.test.CacheField.class);
                    final String destination = Optional.of(annotation.destination())
                            .filter(x -> !x.isEmpty())
                            .orElse(originalField.getName() + "Name");
                    Field destinationField = entityClass.getDeclaredField(destination);
                    destinationField.setAccessible(true);
                    list.add(new CacheHandler(annotation.value(), originalField, destinationField));
                }
            } else if (originalField.isAnnotationPresent(CacheEntity.class)) {
                final CacheHandler cacheHandler = new CacheHandler();
                cacheHandler.setOriginalField(originalField);
                list.add(cacheHandler);
            }
        }
        final Class<?> superclass = entityClass.getSuperclass();
        if (ConvertedAble.class.isAssignableFrom(superclass)) {
            return reflectEntity(superclass, list);
        } else {
            return list;
        }
    }

    public static <T> void solveCacheByHandler(T o, List<CacheHandler> list) throws IllegalAccessException, NoSuchFieldException {
        for (CacheHandler cacheHandler : list) {
            Object fieldValue = cacheHandler.getOriginalField().get(o);
            if (fieldValue != null) {
                String dictKey = cacheHandler.getDictKey();
                if (dictKey == null) {
                    convertByType(fieldValue);
                } else {
                    cacheHandler.getDestinationField().set(o, CacheUtil.getCacheValue(fieldValue, dictKey));
                }
            }
        }
    }
}
