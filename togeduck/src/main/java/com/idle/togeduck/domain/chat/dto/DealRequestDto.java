package com.idle.togeduck.domain.chat.dto;

public record DealRequestDto(
	TradeDto myTrade,
	TradeDto trade
) {
}
