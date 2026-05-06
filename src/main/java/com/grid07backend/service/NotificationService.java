package com.grid07backend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final StringRedisTemplate redisTemplate;

    public void handleNotification(Long userId, String message) {
        String cooldown = "notif:user:" + userId;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldown))) {
            redisTemplate.opsForList()
                    .rightPush("user:" + userId + ":pending", message);
        } else {
            System.out.println("Push Notification Sent to User");
            redisTemplate.opsForValue()
                    .set(cooldown, "1", Duration.ofMinutes(15));
        }
    }
}
