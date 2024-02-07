package com.idle.togeduck.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.chat.entity.UserChat;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {
	Optional<UserChat> findByUserIdAndChatId(Long userId, Long chatId);

	Long countByChatId(Long chatId);
}
