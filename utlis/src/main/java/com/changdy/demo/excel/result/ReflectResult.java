package com.changdy.demo.excel.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * Created by Changdy on 2019/8/14.
 */
@Data
@NoArgsConstructor
public class ReflectResult {

    private Field field;
    private String name;
    private BiFunction<Object, Object, String> function;


    public ReflectResult(Field field, String name) {
        this.field = field;
        this.name = name;
    }
}
