package com.idle.togeduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.chat.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
