package com.idle.togeduck.domain.chat.dto;

public record MessageDto(
	Long chatId,
	String content
) {
}
