package com.saysmile.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redisTemplate;

    public void storeToken(String token, String username) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        // store token in redis for 10 hours
        ops.set("jwt:" + username, token, Duration.ofHours(10));
    }

    public boolean isTokenValid(String token, String username) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedToken = ops.get("jwt:" + username);
        return token.equals(storedToken);
    }

    public void blacklistToken(String username) {
        redisTemplate.delete("jwt:" + username);
    }
}
