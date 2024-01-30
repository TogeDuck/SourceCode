package com.idle.togeduck.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),
	INTERNAL_SERVER_ERROR(500, "COMMON-002", "서버에서 처리할 수 없는 경우"),

	DUPLICATE_LOGIN_ID(400, "USER-001", "계정명이 중복된 경우"),
	UNAUTHORIZED(401, "USER-002", "인증에 실패한 경우"),
	USER_NOT_FOUND(404, "USER-003", "유저를 찾을 수 없는 경우"),
	TOKEN_NOT_EXISTS(404, "USER-004", "토큰이 존재하지 않는 경우"),

	EVENT_NOT_FOUND(404, "EVENT-001", "이벤트를 찾을 수 없는 경우"),

	SHARE_NOT_FOUND(404, "SHARE-001", "이벤트를 찾을 수 없는 경우");

	private final int status;
	private final String code;
	private final String message;

}
