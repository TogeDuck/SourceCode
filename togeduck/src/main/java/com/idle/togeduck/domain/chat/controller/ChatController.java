package com.idle.togeduck.domain.chat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.chat.dto.ChatRoomResponseDto;
import com.idle.togeduck.domain.chat.dto.MessageRequestDto;
import com.idle.togeduck.domain.chat.dto.MessageResponseDto;
import com.idle.togeduck.domain.chat.service.ChatService;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@GetMapping("/events/chats")
	public ResponseEntity<BaseResponse<List<ChatRoomResponseDto>>> getChatRooms(@AuthenticationPrincipal User user) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "채팅방 정보를 가져왔습니다", chatService.getChatRooms(user.getId())));
	}

	//모집 참여
	@MessageMapping("/chats/{chatId}/join")
	@SendTo("/sub/chats/{chatId}")
	public MessageResponseDto join(@DestinationVariable Long chatId,
		MessageRequestDto messageRequestDto,
		@Header("Authorization") String Authorization) {
		chatService.join(messageRequestDto.chatId(), 1L, messageRequestDto.content());
		return new MessageResponseDto(messageRequestDto.chatId(), 1L, messageRequestDto.content());
	}

	@MessageMapping("/chats/{chatId}/message")
	@SendTo("/sub/chats/{chatId}")
	public MessageResponseDto send(@DestinationVariable Long chatId,
		MessageRequestDto messageRequestDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		chatService.message(messageRequestDto.chatId(), 1L, messageRequestDto.content());
		return new MessageResponseDto(messageRequestDto.chatId(), 1L, messageRequestDto.content());
	}

	@MessageMapping("/chats/{chatId}/leave")
	@SendTo("/sub/chats/{chatId}")
	public MessageResponseDto leave(@DestinationVariable Long chatId,
		MessageRequestDto messageRequestDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		chatService.leave(messageRequestDto.chatId(), 1L, messageRequestDto.content());
		return new MessageResponseDto(messageRequestDto.chatId(), 1L, messageRequestDto.content());
	}
}
