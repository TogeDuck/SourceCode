package com.idle.togeduck.domain.user.dto;

import lombok.Builder;

@Builder
public record GoogleRequestAccessTokenDto(
	String code,
	String clientId,
	String clientSecret,
	String redirectUri,
	String grantType
) {
}
