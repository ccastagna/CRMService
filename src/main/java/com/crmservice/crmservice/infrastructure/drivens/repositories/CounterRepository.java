package com.crmservice.crmservice.infrastructure.drivens.repositories;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public class CounterRepository implements ICounterRepository {

    private final RedisTemplate<String, Integer> redisTemplate;

    public CounterRepository(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Integer get(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    public void save(String key, Integer value, Duration ttl) {
        this.redisTemplate.opsForValue().set(key, value, ttl);
    }
}
