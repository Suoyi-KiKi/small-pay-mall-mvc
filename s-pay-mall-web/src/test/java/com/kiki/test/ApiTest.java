package com.kiki.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class ApiTest {

    @Resource
    RedisTemplate<String,String> template;

    @Test
    void Test(){
        template.opsForValue().set("hello", "kiki");
    }
}
