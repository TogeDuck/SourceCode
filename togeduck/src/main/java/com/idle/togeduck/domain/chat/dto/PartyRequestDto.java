package com.idle.togeduck.domain.chat.dto;

public record PartyRequestDto(
	String title,
	Long destinationId,
	int maximum,
	int duration
) {
}
