package com.idle.togeduck.domain.event.dto;

public record ShareRespondDto(
	Long id,
	String image,
	String title,
	String content,
	Integer duration,
	boolean isMine
) {
}
