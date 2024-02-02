package com.idle.togeduck.domain.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.chat.dto.MessageDto;
import com.idle.togeduck.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	//모집 참여
	@MessageMapping("/chats/{chatId}/join")
	@SendTo("/sub/chats/{chatId}")
	public MessageDto join(@DestinationVariable Long chatId,
		MessageDto messageDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		chatService.join(messageDto.chatId(), 1L, messageDto.content());
		return messageDto;
	}

	@MessageMapping("/chats/{chatId}/message")
	@SendTo("/sub/chats/{chatId}")
	public MessageDto send(@DestinationVariable Long chatId,
		MessageDto messageDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		chatService.message(messageDto.chatId(), 1L, messageDto.content());
		return messageDto;
	}

	@MessageMapping("/chats/{chatId}/leave")
	@SendTo("/sub/chats/{chatId}")
	public MessageDto leave(@DestinationVariable Long chatId,
		MessageDto messageDto,
		@Header("Authorization") String Authorization) {
		//유저 아이디 추후 토큰에서 받아오기
		chatService.leave(messageDto.chatId(), 1L, messageDto.content());
		return messageDto;
	}
}
