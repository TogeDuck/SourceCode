package com.idle.togeduck.domain.chat.dto;

public record ChatRoomResponseDto(
	Long chatId,
	Long eventId,
	String title,
	String dtype
) {
}
