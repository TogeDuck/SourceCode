package com.idle.togeduck.domain.celebrity.dto;

public record LocationResponseDto(
	Long celebrityId,
	Long userId,
	Double latitude,
	Double longitude
) {
}
