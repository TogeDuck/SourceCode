package com.idle.togeduck.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.user.dto.FavoriteRequestDto;
import com.idle.togeduck.domain.user.serivce.UserService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService uesrService;

	@GetMapping("/favorities")
	public ResponseEntity<BaseResponse<?>> getFavorite(
		@RequestBody FavoriteRequestDto favoriteRequestDto) {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success",
				uesrService.getFavoriteCelebrity(favoriteRequestDto)
			));
	}

	@PostMapping("/favorities")
	public ResponseEntity<BaseResponse<?>> upsertFavorite(
		@RequestBody FavoriteRequestDto favoriteRequestDto) { // update + insert

		uesrService.upsertFavorite(favoriteRequestDto);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", null));
	}

}
