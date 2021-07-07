package com.changdy.demo.convert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheHandler {
    private String dictKey;
    private Field originalField;
    private Field destinationField;
}