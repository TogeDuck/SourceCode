package com.idle.togeduck.domain.event.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.event.dto.TodayAndLaterEventResponseDto;
import com.idle.togeduck.domain.event.service.EventService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
	public final EventService eventService;

	@GetMapping
	public ResponseEntity<BaseResponse<TodayAndLaterEventResponseDto>> getEvents(
		@RequestParam(name = "celebrity-id") Long celebrityId,
		@RequestParam(name = "start-date") LocalDate startDate,
		@RequestParam(name = "end-date") LocalDate endDate,
		Long userId) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(
				new BaseResponse<>(200, "이벤트 조회 완료", eventService.getEvents(celebrityId, startDate, endDate, userId)));
	}
}
