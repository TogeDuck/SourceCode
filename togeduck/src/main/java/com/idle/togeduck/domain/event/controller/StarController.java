package com.idle.togeduck.domain.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.event.dto.AllEventResponseDto;
import com.idle.togeduck.domain.event.service.StarService;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class StarController {
	private final StarService starService;

	@GetMapping("/likes")
	public ResponseEntity<BaseResponse<AllEventResponseDto>> getEvents(@AuthenticationPrincipal User user) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "즐겨찾기 조회 완료", starService.getEvents(user.getId())));
	}

	@PostMapping("/likes")
	public ResponseEntity<BaseResponse<?>> addStar(@RequestParam(value = "event_id") Long eventId,
		@AuthenticationPrincipal User user) {

		starService.addStar(user.getId(), eventId);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new BaseResponse<>(201, "즐겨찾기 등록 완료", null));
	}

	@DeleteMapping("/likes/{eventId}")
	public ResponseEntity<BaseResponse<?>> deleteStar(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
		starService.deleteStar(eventId);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "즐겨찾기 삭제가 성공했습니다", null));
	}

}
