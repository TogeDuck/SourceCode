package com.idle.togeduck.domain.event.dto;

import java.time.LocalDateTime;

public record ShareRespondDto(
	Long id,
	String image,
	String title,
	String content,
	LocalDateTime createdAt,
	boolean isMine
) {
}
