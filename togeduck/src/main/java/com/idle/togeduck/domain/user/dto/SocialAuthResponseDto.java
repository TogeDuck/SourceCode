package com.idle.togeduck.domain.user.dto;

public record SocialAuthResponseDto(
	String accessToken,
	String tokenType,
	String refreshToken,
	String expiresIn,
	String scope,
	String refreshTokenExpiresIn
) {
}
