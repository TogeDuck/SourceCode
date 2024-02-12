package com.idle.togeduck.domain.user.serivce;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate redisTemplate;
	private final RedisTemplate<String, Object> redisBlackListTemplate;

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

	public void setBlackList(String key, Object object, Long milliSeconds) {
		redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(object.getClass()));
		redisBlackListTemplate.opsForValue().set(key, object, milliSeconds, TimeUnit.MILLISECONDS);
	}

	public boolean hasKeyBlackList(String key) {
		return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
	}
}
