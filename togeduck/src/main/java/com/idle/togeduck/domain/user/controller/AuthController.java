package com.idle.togeduck.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.user.dto.LoginRequestDto;
import com.idle.togeduck.domain.user.dto.TokenRequestDto;
import com.idle.togeduck.domain.user.serivce.AuthService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<?>> login(@RequestBody LoginRequestDto loginRequestDto) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", authService.login(loginRequestDto)));
	}

	@PostMapping("/reissue")
	public ResponseEntity<BaseResponse<?>> reissue(@RequestBody TokenRequestDto tokenRequestDtoDto) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", authService.reissue(tokenRequestDtoDto)));
	}
}
