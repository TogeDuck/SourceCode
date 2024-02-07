package com.idle.togeduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
