package com.idle.togeduck.domain.event.dto;

public record ReviewResponseDto(
	Long id,
	String content,
	Boolean isMine,
	String url
) {
}
