package com.idle.togeduck.domain.user.dto;

import com.idle.togeduck.domain.user.entity.Authority;
import com.idle.togeduck.domain.user.entity.SocialType;

public record UserResponseDto(
	Long Id,
	String socialId,
	SocialType socialType,
	Authority authority
) {
}
