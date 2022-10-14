package com.dms.guyiyao.daoTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class redisTest {

    private final RedisTemplate<String, Object> redisTemplate;//常量需要使用构造方法注入

    public redisTest(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Test
    public void testConnect(){
        redisTemplate.opsForValue().set("hello","hi");

}
}
