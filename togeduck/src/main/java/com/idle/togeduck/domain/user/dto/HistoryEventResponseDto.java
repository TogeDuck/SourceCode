package com.idle.togeduck.domain.user.dto;

public record HistoryEventResponseDto(
	Long event_id,
	String name,
	Double latitude,
	Double longitude
) {
}
