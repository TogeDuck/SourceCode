package com.idle.togeduck.domain.celebrity.dto;

import lombok.Builder;

@Builder
public record CelebrityRequestDto(
	String keyword
) {
}
