package com.idle.togeduck.domain.event.dto;

import java.util.List;

public record AllEventResponseDto(
	List<JoinEventResponseDto> past,
	List<JoinEventResponseDto> today,
	List<JoinEventResponseDto> later
) {
}
