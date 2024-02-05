package com.idle.togeduck.domain.event.dto;

import java.util.List;

public record TodayAndLaterEventResponseDto(
	List<JoinEventResponseDto> today,
	List<JoinEventResponseDto> later
) {
}
