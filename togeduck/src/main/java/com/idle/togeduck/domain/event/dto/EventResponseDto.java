package com.idle.togeduck.domain.event.dto;

import java.time.LocalDate;

public record EventResponseDto(
	Long eventId,
	String url,
	String name,
	LocalDate startDate,
	LocalDate endDate,
	Double latitude,
	Double longitude
) {
}
