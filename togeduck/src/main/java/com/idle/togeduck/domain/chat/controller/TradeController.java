package com.idle.togeduck.domain.chat.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.chat.dto.TradeRequestDto;
import com.idle.togeduck.domain.chat.dto.TradeResponseDto;
import com.idle.togeduck.domain.chat.service.TradeService;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class TradeController {

	private final TradeService tradeService;

	@GetMapping("/{eventId}/trades")
	public ResponseEntity<BaseResponse<Slice<TradeResponseDto>>> getTradeList(
		@PathVariable Long eventId,
		Pageable pageable) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "성공", tradeService.getTradeList(eventId, pageable)));
	}

	@GetMapping("/{eventId}/trades/{tradeId}")
	public ResponseEntity<BaseResponse<TradeResponseDto>> getTrade(
		@PathVariable Long eventId,
		@PathVariable Long tradeId) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "성공", tradeService.getTrade(tradeId)));
	}

	@PostMapping("/{eventId}/trades")
	public ResponseEntity<BaseResponse<?>> createTrade(
		@PathVariable Long eventId,
		@RequestPart MultipartFile image,
		@RequestPart TradeRequestDto tradeRequestDto) {

		User user = User.builder().id(1L).build();

		tradeService.createTrade(eventId, user, image, tradeRequestDto.content(), tradeRequestDto.duration());

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@PatchMapping("/{eventId}/trades/{tradeId}")
	public ResponseEntity<BaseResponse<?>> updateTrade(
		@PathVariable Long eventId,
		@PathVariable Long tradeId,
		@RequestPart MultipartFile image,
		@RequestPart TradeRequestDto tradeRequestDto) {

		User user = User.builder().id(1L).build();

		tradeService.updateTrade(tradeId, user, image, tradeRequestDto.content(), tradeRequestDto.duration());

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@DeleteMapping("/{eventId}/trades/{tradeId}")
	public ResponseEntity<BaseResponse<?>> deleteTrade(
		@PathVariable Long eventId,
		@PathVariable Long tradeId) {

		User user = User.builder().id(1L).build();

		tradeService.deleteTrade(tradeId, user);

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}
}
