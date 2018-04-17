package com.changdy.demo;

import com.changdy.demo.mapper.inner.InnerMapper;
import com.changdy.demo.mapper.outer.OuterMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private InnerMapper innerMapper;

    @Autowired
    private OuterMapper outerMapper;
    @Test
    public void contextLoads() {
        System.out.println(innerMapper.testMapper());
        System.out.println(outerMapper.testMapper());
    }
}
