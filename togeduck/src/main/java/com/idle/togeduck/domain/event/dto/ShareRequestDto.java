package com.idle.togeduck.domain.event.dto;

public record ShareRequestDto(
	String title,
	String content,
	Integer duration
) {
}
