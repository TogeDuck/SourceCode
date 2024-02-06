package com.idle.togeduck.domain.user.dto;

public record HistoryEventRequestDto(
	Long eventId,
	Long historyId
) {
}
