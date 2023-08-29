package com.sparta.petnexus.common.redis.utils;

import java.time.Duration;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    private final ModelMapper modelMapper;

    public void put(String key, Object value, Duration expiredAt){
        if(expiredAt != null){
            redisTemplate.opsForValue().set(key, value, expiredAt.toMillis(), TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public <T> T get(String key, Class<T> clazz){
        Object o = redisTemplate.opsForValue().get(key);
        if(o != null) {
            if(o instanceof LinkedHashMap){
                return modelMapper.map(o, clazz);
            }else{
                return clazz.cast(o);
            }
        }
        return null;
    }

    public boolean isExists(String key){
        return redisTemplate.hasKey(key);
    }

    public void setExpireTime(String key, long expirationTime){
        redisTemplate.expire(key, expirationTime, TimeUnit.SECONDS);
    }

    public long getExpireTime(String key){
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public void setBlackList(String key, Object value, Duration expiredAt) {
        redisBlackListTemplate.opsForValue().set(key, value, expiredAt.toMillis(), TimeUnit.SECONDS);
    }
    public boolean hasKeyBlackList(String key){
        return redisBlackListTemplate.hasKey(key);
    }
}
