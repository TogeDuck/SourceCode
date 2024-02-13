package com.idle.togeduck.domain.celebrity.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.dto.CountResponseDto;
import com.idle.togeduck.domain.celebrity.dto.LocationRequestDto;
import com.idle.togeduck.domain.celebrity.dto.LocationResponseDto;
import com.idle.togeduck.domain.celebrity.service.CelebrityService;
import com.idle.togeduck.domain.celebrity.service.CelebrityUserService;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.jwt.JwtProvider;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/celebrities")
@RequiredArgsConstructor
public class CelebrityController {

	private final CelebrityService celebrityService;
	private final CelebrityUserService celebrityUserService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final JwtProvider jwtProvider;

	@GetMapping("/search")
	public ResponseEntity<BaseResponse<List<CelebrityResponseDto>>> getAllCelebrity(@RequestParam String keyword
	) throws IOException {

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", celebrityService.getCelebrityByKeyword(keyword)));
	}

	@GetMapping("/user")
	public User currentUserName(@AuthenticationPrincipal User user) {
		return user;
	}

	@MessageMapping("/celebrities/{celebrityId}/join")
	@SendTo("/sub/celebrities/{celebrityId}")
	public LocationResponseDto join(@DestinationVariable Long celebrityId,
		LocationRequestDto locationRequestDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		Long userId = jwtProvider.getUserIdFromToken(Authorization);
		celebrityUserService.join(celebrityId, locationRequestDto, userId);
		simpMessagingTemplate.convertAndSend("/sub/celebrities/" + celebrityId + "/count",
			new CountResponseDto(celebrityUserService.count(celebrityId)));
		return new LocationResponseDto(celebrityId, userId, locationRequestDto.latitude(),
			locationRequestDto.longitude());
	}

	@MessageMapping("/celebrities/{celebrityId}/message")
	@SendTo("/sub/celebrities/{celebrityId}")
	public LocationResponseDto message(@DestinationVariable Long celebrityId,
		LocationRequestDto locationRequestDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		Long userId = jwtProvider.getUserIdFromToken(Authorization);
		celebrityUserService.message(celebrityId, locationRequestDto, userId);
		return new LocationResponseDto(celebrityId, userId, locationRequestDto.latitude(),
			locationRequestDto.longitude());
	}

	@MessageMapping("/celebrities/{celebrityId}/leave")
	@SendTo("/sub/celebrities/{celebrityId}")
	public LocationResponseDto leave(@DestinationVariable Long celebrityId,
		LocationRequestDto locationRequestDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		Long userId = jwtProvider.getUserIdFromToken(Authorization);
		celebrityUserService.leave(celebrityId, locationRequestDto, userId);
		simpMessagingTemplate.convertAndSend("/sub/celebrities/" + celebrityId + "/count",
			new CountResponseDto(celebrityUserService.count(celebrityId)));
		return new LocationResponseDto(celebrityId, userId, locationRequestDto.latitude(),
			locationRequestDto.longitude());
	}
}
