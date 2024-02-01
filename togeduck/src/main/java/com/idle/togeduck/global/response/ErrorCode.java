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
	REVIEW_NOT_FOUND(404, "REVIEW-001", "리뷰를 찾을 수 없는 경우"),
	DATA_CANT_SAVE(404, "S3-001", "데이터 저장에 실패 - 필드값 확인 OR 연관 엔티티 확인"),
	SHARE_NOT_FOUND(404, "SHARE-001", "이벤트를 찾을 수 없는 경우"),
	TRADE_NOT_FOUND(404, "TRADE-001", "교환을 찾을 수 없는 경우"),
	PARTY_NOT_FOUND(404, "PARTY-001", "모집을 찾을 수 없는 경우"),
	CELEBRITY_NOT_FOUND(404, "CELEBRITY-001", "연예인을 찾을 수 없는 경우"),
	STAR_NOT_FOUND(404, "STAR-001", "해당 즐겨찾기가 없습니다");
	private final int status;
	private final String code;
	private final String message;

}
