package com.indytskyi.userserviceairport.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class LogoutRepositoryImpl implements LogoutRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${jwt.token.expiredTime}")
    private Long tokenExpiredPeriodMs;

    @Override
    public void addUser(String token, String email) {
        redisTemplate.opsForValue().set(token, email, tokenExpiredPeriodMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isLoggedOut(String token) {
        return redisTemplate.opsForValue().get(token) != null;
    }
}
