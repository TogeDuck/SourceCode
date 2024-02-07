package com.idle.togeduck.domain.event.dto;

import java.time.LocalDate;

public record JoinEventResponseDto(
	Long eventId,
	String name,
	String image1,
	String image2,
	String image3,
	LocalDate startDate,
	LocalDate endDate,
	Double latitude,
	Double longitude,
	Boolean isStar,
	Boolean isVisited
) {
}