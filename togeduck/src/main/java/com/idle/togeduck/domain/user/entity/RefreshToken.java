package com.idle.togeduck.domain.user.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@RedisHash(value = "refresh_token", timeToLive = 60 * 60 * 24 * 3)
public class RefreshToken {

	@Id
	private final String userId;
	private final String refreshToken;
}
