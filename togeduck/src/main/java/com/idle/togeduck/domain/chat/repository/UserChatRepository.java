package com.idle.togeduck.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idle.togeduck.domain.chat.dto.ChatRoomResponseDto;
import com.idle.togeduck.domain.chat.entity.UserChat;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {
	Optional<UserChat> findByUserIdAndChatId(Long userId, Long chatId);

	Long countByChatId(Long chatId);

	@Query(value = "select c.chat_id, q.quest_id, q.title, q.dtype from quest q left join chat c on q.quest_id=c.quest_id where c.chat_id in ( select uc.chat_id from user_chat uc where uc.user_id=:userId)", nativeQuery = true)
	List<ChatRoomResponseDto> findListByUserId(Long userId);
}
