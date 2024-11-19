package com.teamjhw.bestfriend.domain.member.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisMailService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.data.redis.duration}")
    private int duration;

    /**
     * Redis에서 key 값으로 조회
     * */
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * Redis에서 key 값으로 삭제
     * */
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Redis에 값 생성/수정
     * */
    public void saveData(String key, String value){
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))) deleteData(key);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expiredAt = Duration.ofMinutes(duration);
        valueOperations.set(key, value, expiredAt);
    }
}
