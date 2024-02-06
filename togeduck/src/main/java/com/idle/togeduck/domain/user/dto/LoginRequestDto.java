package com.idle.togeduck.domain.user.dto;

import com.idle.togeduck.domain.user.entity.SocialType;

public record LoginRequestDto(
	SocialType socialType,
	String code
) {
}
