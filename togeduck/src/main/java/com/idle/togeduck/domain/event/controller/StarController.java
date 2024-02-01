package com.idle.togeduck.domain.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.event.dto.AllEventResponseDto;
import com.idle.togeduck.domain.event.service.StarService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class StarController {
	private final StarService starService;

	@GetMapping("/likes")
	public ResponseEntity<BaseResponse<AllEventResponseDto>> getEvents(Long userId) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "즐겨찾기 조회 완료", starService.getEvents(userId)));
	}

	@PostMapping("/likes")
	public ResponseEntity<BaseResponse<?>> addStar(@RequestParam Long eventId, Long userId) {

		starService.addStar(userId, eventId);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new BaseResponse<>(201, "즐겨찾기 등록 완료", null));
	}

	@DeleteMapping("/likes/{likeId}")
	public ResponseEntity<BaseResponse<?>> deleteStar(@PathVariable Long likeId, Long userId) {
		starService.deleteStar(likeId, userId);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "즐겨찾기 삭제가 성공했습니다", null));
	}

}
