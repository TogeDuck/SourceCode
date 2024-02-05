package com.idle.togeduck.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.user.dto.HistoryEventRequestDto;
import com.idle.togeduck.domain.user.dto.HistoryResponseDto;
import com.idle.togeduck.domain.user.dto.RouteResponseDto;
import com.idle.togeduck.domain.user.serivce.HistoryService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class HistoryController {
	private final HistoryService historyService;

	@GetMapping("/history")
	public ResponseEntity<BaseResponse<?>> createHistory(@RequestParam("celebrity-id") Long celebrityId, Long userId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 생성이 완료되었습니다.", historyService.createHistory(celebrityId, userId)));
	}

	@PostMapping("/history-event")
	public ResponseEntity<BaseResponse<?>> addVisitedEvent(@RequestBody HistoryEventRequestDto historyEventRequestDto) {
		historyService.addVisitedEvent(historyEventRequestDto.historyId(), historyEventRequestDto.eventId());

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 경로 등록이 완료되었습니다", null));
	}

	@GetMapping("/history-event")
	public ResponseEntity<BaseResponse<List<HistoryResponseDto>>> joinHistory(
		@RequestParam("celebrity-id") Long celebrityId,
		Long userId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "진행했던 투어 경로 정보들을 가져왔습니다.", historyService.joinHistory(celebrityId, userId)));
	}

	@GetMapping("/history/{history_id}")
	public ResponseEntity<BaseResponse<RouteResponseDto>> getRoutes(@PathVariable(name = "history_id") Long historyId,
		Long userId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 방문 이벤트 정보를 가져왔습니다.", historyService.getRoutes(historyId, userId)));
	}

	// @PatchMapping
	// public ResponseEntity<BaseResponse<?>> updateRoute(@RequestBody String route, Long historyId) {
	// 	historyService.updateRoute(route, historyId);
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 		.body(new BaseResponse<>(200, "투어 경로 등록이 완료되었습니다.", null));
	// }
}
