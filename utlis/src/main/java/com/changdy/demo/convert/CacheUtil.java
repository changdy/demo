package com.changdy.demo.convert;

public class CacheUtil {
    public static String getCacheValue(Object key, String dictCode) {
        if (key == null) {
            return null;
        }
        return new StringBuilder(String.valueOf(key)).reverse().append(dictCode).toString();
    }
}
