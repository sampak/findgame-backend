package com.sampak.gameapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampak.gameapp.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> void save(String key, T object) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public <T> void saveWithTTL(String key, T object, long timeout, TimeUnit unit) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonString, timeout, unit);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        String jsonString = redisTemplate.opsForValue().get(key);
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
