package com.sampak.gameapp.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    <T> void save(String key, T object);
    <T> void saveWithTTL(String key, T object, long timeout, TimeUnit unit);
    <T> T get(String key, Class<T> clazz);
}
