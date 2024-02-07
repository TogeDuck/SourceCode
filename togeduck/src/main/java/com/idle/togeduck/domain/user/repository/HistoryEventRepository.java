package com.idle.togeduck.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.user.entity.HistoryEvent;

public interface HistoryEventRepository extends JpaRepository<HistoryEvent, Long>, HistoryEventRepositoryCustom {
	void deleteAllByHistoryId(Long historyId);
}
