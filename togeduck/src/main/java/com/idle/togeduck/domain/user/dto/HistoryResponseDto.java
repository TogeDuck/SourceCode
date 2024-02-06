package com.idle.togeduck.domain.user.dto;

import java.time.LocalDate;

public record HistoryResponseDto(
	Long historyId,
	String historyName,
	LocalDate date
) {
}
