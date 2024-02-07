package com.idle.togeduck.domain.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.user.dto.HistoryEventRequestDto;
import com.idle.togeduck.domain.user.dto.HistoryResponseDto;
import com.idle.togeduck.domain.user.dto.RouteResponseDto;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.serivce.HistoryService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class HistoryController {
	private final HistoryService historyService;

	@GetMapping("/history")
	public ResponseEntity<BaseResponse<?>> createHistory(@RequestParam("celebrity-id") Long celebrityId,
		@AuthenticationPrincipal User user) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 생성이 완료되었습니다.", historyService.createHistory(celebrityId, user.getId())));
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
		@AuthenticationPrincipal User user) {

		System.out.println("1 = " + 1);

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "진행했던 투어 경로 정보들을 가져왔습니다.",
				historyService.joinHistory(celebrityId, user.getId())));
	}

	@GetMapping("/history/{history_id}")
	public ResponseEntity<BaseResponse<RouteResponseDto>> getRoutes(@PathVariable(name = "history_id") Long historyId,
		@AuthenticationPrincipal User user) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 방문 이벤트 정보를 가져왔습니다.", historyService.getRoutes(historyId, user.getId())));
	}

	@PatchMapping("/history/{history_id}/name")
	public ResponseEntity<BaseResponse<?>> updateName(@PathVariable(name = "history_id") Long historyId,
		@RequestParam("history_name") String historyName, @AuthenticationPrincipal User user) {

		historyService.updateName(historyId, historyName, user.getId());
		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 정보를 수정했습니다.", null));
	}

	@PatchMapping("/history/{history_id}/route")
	public ResponseEntity<BaseResponse<?>> updateRoute(@PathVariable(name = "history_id") Long historyId,
		String route, @AuthenticationPrincipal User user) {

		historyService.updateRoute(historyId, route, user.getId());
		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 루트를 추가했습니다.", null));
	}

	@DeleteMapping("/history/{history_id}")
	public ResponseEntity<BaseResponse<?>> deleteHistory(@PathVariable(name = "history_id") Long historyId,
		@AuthenticationPrincipal User user) {
		historyService.deleteHistory(historyId, user.getId());
		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "투어 정보를 삭제했습니다.", null));
	}

	// @PatchMapping
	// public ResponseEntity<BaseResponse<?>> updateRoute(@RequestBody String route, Long historyId) {
	// 	historyService.updateRoute(route, historyId);
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 		.body(new BaseResponse<>(200, "투어 경로 등록이 완료되었습니다.", null));
	// }
}