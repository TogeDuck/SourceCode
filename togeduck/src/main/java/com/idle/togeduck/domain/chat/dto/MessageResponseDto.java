package com.idle.togeduck.domain.chat.dto;

public record MessageResponseDto(
	Long chatId,
	Long userId,
	String content
) {
}
