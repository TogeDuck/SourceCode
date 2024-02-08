package com.idle.togeduck.domain.celebrity.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.service.CelebrityService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/celebrities")
@RequiredArgsConstructor
public class CelebrityController {

	private final CelebrityService celebrityService;

	@GetMapping("/search")
	public ResponseEntity<BaseResponse<List<CelebrityResponseDto>>> getAllCelebrity(
		@RequestParam String keyword) throws
		IOException {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", celebrityService.getAllCelebrity(keyword)));
	}
}
