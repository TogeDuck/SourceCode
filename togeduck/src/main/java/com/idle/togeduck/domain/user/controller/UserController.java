package com.idle.togeduck.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.user.dto.UserRequestDto;
import com.idle.togeduck.domain.user.serivce.UserService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<BaseResponse<?>> getUser(@RequestBody UserRequestDto userRequestDto) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", userService.getUserBySocialId(userRequestDto.socialId())));
	}

	@PostMapping
	public ResponseEntity<BaseResponse<?>> insertUid(@RequestBody UserRequestDto userRequestDto) {

		userService.insertUserBySocialId(userRequestDto);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", ""));
	}
}
