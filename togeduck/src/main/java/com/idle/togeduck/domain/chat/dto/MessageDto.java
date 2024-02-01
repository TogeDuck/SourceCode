package com.idle.togeduck.domain.chat.dto;

public record MessageDto(
	Long chatId,
	Long userId,
	String content
) {
}
