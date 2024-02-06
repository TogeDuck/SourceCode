package com.idle.togeduck.domain.user.dto;

import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {

	@Id
	private String id;
	private String refreshToken;
	@Indexed
	private String accessToken;
}
