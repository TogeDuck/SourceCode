package com.idle.togeduck.domain.event.dto;

import java.util.List;

public record AllEventResponseDto(
	List<EventResponseDto> past,
	List<EventResponseDto> today,
	List<EventResponseDto> later
) {
}
