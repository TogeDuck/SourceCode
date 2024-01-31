package com.idle.togeduck.domain.user.dto;

public record FavoriteRequestDto(
	Long userId,
	Long celebrityId
) {
}
