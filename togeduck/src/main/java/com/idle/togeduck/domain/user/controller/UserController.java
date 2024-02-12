package com.idle.togeduck.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.user.dto.FavoriteRequestDto;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.serivce.UserService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/favorities")
	public ResponseEntity<BaseResponse<?>> getFavorite(
		@AuthenticationPrincipal User user) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success",
				userService.getFavoriteCelebrity(user)
			));
	}

	@PostMapping("/favorities")
	public ResponseEntity<BaseResponse<?>> upsertFavorite(
		@RequestBody FavoriteRequestDto favoriteRequestDto, @AuthenticationPrincipal User user) { // update + insert

		userService.upsertFavorite(favoriteRequestDto, user.getId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", getFavorite(user).getBody().getData()));
	}

}
