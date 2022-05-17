package com.stackfarm.esports.cache.sql;

import com.stackfarm.esports.system.SystemConstant;
import com.stackfarm.esports.a.SpringUtils;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author 十三月之夜
 */
public abstract class SqlCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static final String SQL_CACHE_PREFIX = "SQL_CACHE_PREFIX_";

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void putObject(Object key, Object value) {
        redisTemplate = getRedisTemplate();
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set(key.toString(), value, SystemConstant.SQL_CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    @Override
    public Object getObject(Object key) {
        redisTemplate = getRedisTemplate();
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        return opsForValue.get(key.toString());
    }

    @Override
    public Object removeObject(Object key) {
        redisTemplate = getRedisTemplate();
        redisTemplate.delete(key.toString());
        return null;
    }

    @Override
    public void clear() {
        redisTemplate = getRedisTemplate();
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }

    public void clear(String pattern) {
        Set<String> keys = getRedisTemplate().keys(pattern);
        assert keys != null;
        getRedisTemplate().delete(keys);
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean("redisTemplate");
        }
        return redisTemplate;
    }
}
