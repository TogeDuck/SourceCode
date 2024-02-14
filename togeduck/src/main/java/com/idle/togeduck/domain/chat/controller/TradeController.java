package com.idle.togeduck.domain.chat.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.chat.dto.DealRequestDto;
import com.idle.togeduck.domain.chat.dto.TradeRequestDto;
import com.idle.togeduck.domain.chat.dto.TradeResponseDto;
import com.idle.togeduck.domain.chat.service.DealService;
import com.idle.togeduck.domain.chat.service.TradeService;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class TradeController {

	private final TradeService tradeService;
	private final DealService dealService;

	@GetMapping("/{eventId}/trades")
	public ResponseEntity<BaseResponse<Slice<TradeResponseDto>>> getTradeList(
		@PathVariable Long eventId,
		Pageable pageable,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "성공", tradeService.getTradeList(user, eventId, pageable)));
	}

	@GetMapping("/{eventId}/trades/{tradeId}")
	public ResponseEntity<BaseResponse<TradeResponseDto>> getTrade(
		@PathVariable Long eventId,
		@PathVariable Long tradeId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "성공", tradeService.getTrade(user, tradeId)));
	}

	@GetMapping("/{eventId}/trades/mytrades")
	public ResponseEntity<BaseResponse<Slice<TradeResponseDto>>> getMyTradeList(
		@PathVariable Long eventId,
		Pageable pageable,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "성공", tradeService.getMyTradeList(user, eventId, pageable)));
	}

	@PostMapping("/{eventId}/trades")
	public ResponseEntity<BaseResponse<?>> createTrade(
		@PathVariable Long eventId,
		@RequestPart MultipartFile image,
		@RequestPart TradeRequestDto tradeRequestDto,
		@AuthenticationPrincipal User user) {

		tradeService.createTrade(eventId, user, image, tradeRequestDto.content(), tradeRequestDto.duration());

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@PatchMapping("/{eventId}/trades/{tradeId}")
	public ResponseEntity<BaseResponse<?>> updateTrade(
		@PathVariable Long eventId,
		@PathVariable Long tradeId,
		@RequestPart MultipartFile image,
		@RequestPart TradeRequestDto tradeRequestDto,
		@AuthenticationPrincipal User user) {

		tradeService.updateTrade(tradeId, user, image, tradeRequestDto.content(), tradeRequestDto.duration());

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@DeleteMapping("/{eventId}/trades/{tradeId}")
	public ResponseEntity<BaseResponse<?>> deleteTrade(
		@PathVariable Long eventId,
		@PathVariable Long tradeId,
		@AuthenticationPrincipal User user) {

		tradeService.deleteTrade(tradeId, user);

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@PostMapping("/{eventId}/trades/{tradeId}/mytrades/{myTradeId}/requests")
	public ResponseEntity<BaseResponse<?>> createDeal(
		@PathVariable Long eventId,
		@PathVariable Long tradeId,
		@PathVariable Long myTradeId,
		@AuthenticationPrincipal User user) {

		dealService.createDeal(user.getId(), tradeId, myTradeId);

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@PostMapping("/deals/{dealId}/accept")
	public ResponseEntity<BaseResponse<?>> acceptDeal(
		@PathVariable Long dealId,
		@AuthenticationPrincipal User user) {

		dealService.acceptDeal(dealId);

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@PostMapping("/deals/{dealId}/reject")
	public ResponseEntity<BaseResponse<?>> rejectDeal(
		@PathVariable Long dealId,
		@AuthenticationPrincipal User user) {

		dealService.rejectDeal(dealId);

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@GetMapping("/deals/{dealId}")
	public ResponseEntity<BaseResponse<DealRequestDto>> getDeal(
		@PathVariable Long dealId,
		@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "성공", dealService.getDeal(dealId)));
	}
}
