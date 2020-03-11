package com.changdy.springboot;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Changdy on 2018/4/10.
 */
public class TestCoding {
    private SecureRandom random = new SecureRandom();

    @Test
    public void stringUtils() {
        // 添加分隔符
        List<String> test = Arrays.asList("5", "33", "111");
        System.out.println(String.join(",", test));

        // 判断是否真正含有内容
        System.out.println(StringUtils.hasText("  "));
        // 删掉前后空白 类似还有 删左侧,右侧空白
        System.out.println(StringUtils.trimWhitespace(" \t123 "));

        // 字符串模板 其实也可以加format 但是很少这样用
        System.out.println(MessageFormat.format("{0}{1}{2}", 1, true, "字符串"));

        // 左对齐,右侧空格 '-' '8'
        System.out.format("%-8d===\n", random.nextInt(999999));
        System.out.println("12345678===");

        // 右对齐,前填0 '0' '8'
        System.out.format("%08d\n", random.nextInt(999999));
        // 其他还有包括对 正负值,小数位,日期等格式化,不再累述
    }

    @Test
    public void collectUtils() {
        // 主要还包括一些数组和list 的转换.
        System.out.println(CollectionUtils.containsAny(Arrays.asList(1, 2, 3, 4), Arrays.asList(4, 5, 6, 7, 8)));
        // 检测是否空
        CollectionUtils.isEmpty(new HashMap<>());
        CollectionUtils.isEmpty(new ArrayList<>());
        // 数组转换和数组转 stream
        int[] ints = {1, 2, 3, 4};
        List<Integer> list = CollectionUtils.arrayToList(ints);
        Arrays.stream(ints).forEach(System.out::println);
    }

    @Test
    public void objectUtils() {
        // 对象复制
        BeanUtils.copyProperties(new Object(), new Object());
    }

    @Test
    public void readFile() throws IOException {
        File file = ResourceUtils.getFile("classpath:application.yml");
        List<String> readAllLines = Files.readAllLines(Paths.get(new ClassPathResource("application.yml").getURI()));
        for (String line : readAllLines) {
            System.out.println(line);
        }
    }
}