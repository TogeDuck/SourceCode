package com.idle.togeduck.domain.user.dto;

import java.util.List;

public record RouteResponseDto(
	List<Position> route,
	List<HistoryEventResponseDto> history_event
) {
}
