package com.idle.togeduck.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.chat.entity.Chat;
import com.idle.togeduck.domain.chat.entity.Message;
import com.idle.togeduck.domain.chat.entity.UserChat;
import com.idle.togeduck.domain.chat.repository.ChatRepository;
import com.idle.togeduck.domain.chat.repository.MessageRepository;
import com.idle.togeduck.domain.chat.repository.PartyRepository;
import com.idle.togeduck.domain.chat.repository.UserChatRepository;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

	private final PartyRepository partyRepository;
	private final UserRepository userRepository;
	private final UserChatRepository userChatRepository;
	private final MessageRepository messageRepository;
	private final ChatRepository chatRepository;

	@Transactional
	public void join(Long chatId, Long userId, String message) {
		//참여자 수 체크
		Long count = userChatRepository.countByChatId(chatId);
		//채팅 찾기
		Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new BaseException(ErrorCode.CHAT_NOT_FOUND));

		//최대인원보다 크거나 같으면 에러
		if (count >= chat.getMaximum()) {
			throw new BaseException(ErrorCode.CHAT_FULL);
		}
		//유저 찾기
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
		//유저챗 찾기 없으면 생성, 파티 생성자 자동 참여.
		UserChat userChat = userChatRepository.findByUserIdAndChatId(userId, chatId)
			.orElse(UserChat.builder()
				.chat(chat)
				.user(user)
				.build());
		userChat.setDeleted(false);
		userChatRepository.save(userChat);
		//메세지 저장
		Message m = Message.builder()
			.userChat(userChat)
			.content(message)
			.build();
		messageRepository.save(m);
	}

	//채팅 보내기
	@Transactional
	public void message(Long chatId, Long userId, String message) {
		//유저챗 찾기
		UserChat userChat = userChatRepository.findByUserIdAndChatId(userId, chatId)
			.orElseThrow(() -> new BaseException(ErrorCode.USERCHAT_NOT_FOUND));
		//메세지 저장
		Message m = Message.builder()
			.userChat(userChat)
			.content(message)
			.build();
		messageRepository.save(m);
	}

	@Transactional
	public void leave(Long chatId, Long userId, String message) {
		UserChat userChat = userChatRepository.findByUserIdAndChatId(userId, chatId)
			.orElseThrow(() -> new BaseException(ErrorCode.USERCHAT_NOT_FOUND));
		userChat.setDeleted(true);

		Message m = Message.builder()
			.userChat(userChat)
			.content(message)
			.build();
		messageRepository.save(m);
	}
}
