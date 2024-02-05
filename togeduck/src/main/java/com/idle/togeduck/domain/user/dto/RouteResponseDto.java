package com.idle.togeduck.domain.user.dto;

import java.util.List;

public record RouteResponseDto(
	String route,
	List<HistoryEventResponseDto> history_event
) {
}
