package com.idle.togeduck.domain.event.dto;

import java.util.List;

public record AllEventResponseDto(
	List<?> past,
	List<?> today,
	List<?> later
) {
}
