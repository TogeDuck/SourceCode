package com.idle.togeduck.domain.user.serivce;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate redisTemplate;

	public void setValues(String userid, String refreshToken) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(userid, refreshToken); // Duration.ofMinutes(3)
	}

	public String getValues(String userId) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return values.get(userId);
	}

	public void delValues(String userId) {
		redisTemplate.delete(userId);
	}
}
