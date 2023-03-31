package com.example.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final CacheManager cacheManager;

    @Scheduled(cron = "0 * * * * *") // 매 0초마다 / 초, 분, 시, 일, 월, 요일(0-7, 0과 7은 일요일)
    public void cacheEvict() {
        System.out.println("캐시 삭제");
        System.out.println(LocalDateTime.now());
        Cache cache = cacheManager.getCache("team");
        if (cache != null) {
            cache.clear();
        }
    }
}
