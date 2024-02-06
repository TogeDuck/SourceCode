package com.idle.togeduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.chat.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
