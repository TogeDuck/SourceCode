package com.idle.togeduck.domain.chat.dto;

public record MessageRequestDto(
	Long chatId,
	String content
) {
}
