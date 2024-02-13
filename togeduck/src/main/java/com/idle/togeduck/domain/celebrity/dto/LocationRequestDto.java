package com.idle.togeduck.domain.celebrity.dto;

public record LocationRequestDto(
	Long celebrityId,
	Double latitude,
	Double longitude
) {
}
