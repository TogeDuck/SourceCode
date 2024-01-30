package com.idle.togeduck.domain.chat.dto;

import java.time.LocalDateTime;

public record TradeResponseDto(
	Long id,
	String content,
	String image,
	Integer duration,
	LocalDateTime createdAt
) {
}
