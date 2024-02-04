package com.zm.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisLock implements AutoCloseable {
    Logger log = LoggerFactory.getLogger(RedisLock.class);
    RedisTemplate<String, String> redisTemplate;
    private final String key;
    private final String value;
    private final long expireTime;

    public RedisLock(RedisTemplate<String, String> redisTemplate, String key, String value, long expireTime) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
    }

    public Boolean getLock() {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
    }

    public Boolean unLock() {
        // key 是自己才可以释放，不是就不能释放别人的锁
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
        List<String> keys = Collections.singletonList(key);

        // 执行脚本的时候传递的 value 就是对应的值
        Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
        log.info("释放锁的结果：" + result);
        return result;
    }

    @Override
    public void close() throws Exception {
        Boolean result = unLock();
        log.info("释放锁的结果：" + result);
    }
}
