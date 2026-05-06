package com.grid07backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class GuardrailService {
    private final StringRedisTemplate redisTemplate;

    public boolean checkHorizontal(Long postId) {
        String key = "post:" + postId + ":bot_count";

        Long count = redisTemplate.opsForValue().increment(key);

        if (count > 100) {
            redisTemplate.opsForValue().decrement(key);
            return false;
        }
        return true;
    }

    public boolean checkCooldown(Long botId, Long userId) {
        String key = "cooldown:bot_" + botId + ":human_" + userId;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return false;
        }

        redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(10));
        return true;
    }
}
