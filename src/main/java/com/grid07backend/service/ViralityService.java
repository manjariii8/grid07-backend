package com.grid07backend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViralityService {

    private final StringRedisTemplate redisTemplate;

    public void updateScore(Long postId, String type) {
        String key = "post:" + postId + ":virality_score";

        int value = switch (type) {
            case "BOT_REPLY" -> 1;
            case "LIKE" -> 20;
            case "COMMENT" -> 50;
            default -> 0;
        };

        redisTemplate.opsForValue().increment(key, value);
    }
}
