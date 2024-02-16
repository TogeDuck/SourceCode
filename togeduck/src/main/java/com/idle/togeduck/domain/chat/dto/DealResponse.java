package com.idle.togeduck.domain.chat.dto;

public record DealResponse(
	Long id,
	String content,
	String image,
	Long duration,
	Long createdAt,
	Long expiredAt,
	Boolean isMine
) {
}
