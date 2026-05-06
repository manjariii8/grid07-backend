package com.grid07backend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 300000)
    public void sweep() {
        Set<String> keys = redisTemplate.keys("user:*:pending");

        if (keys == null) return;

        for (String key : keys) {
            Long size = redisTemplate.opsForList().size(key);

            if (size != null && size > 0) {
                System.out.println("Summarized Notification: " + size + " interactions");
                redisTemplate.delete(key);
            }
        }
    }
}
