package com.idle.togeduck.domain.user.dto;

import lombok.Builder;

@Builder
public record SocialUserResponseDto(
	String socialId
) {
}
