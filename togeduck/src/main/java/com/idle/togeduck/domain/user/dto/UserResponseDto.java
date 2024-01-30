package com.idle.togeduck.domain.user.dto;

import com.idle.togeduck.domain.user.entity.SocialType;

public record UserResponseDto(

	Long userId,
	String socialId,
	SocialType socialType
) {
}
