package com.idle.togeduck.domain.chat.dto;

import java.time.LocalDateTime;

public record PartyResponseDto(
	Long id,
	String title,
	String destination,
	int maximum,
	int duration,
	Long current,
	LocalDateTime createdAt,
	LocalDateTime expiredAt
) {
}
