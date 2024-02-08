package com.idle.togeduck.domain.celebrity.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CelebrityResponseDto(
	Long id,
	String name,
	String nickname,
	LocalDate birthday,
	String image,
	String teamName
) {
}
