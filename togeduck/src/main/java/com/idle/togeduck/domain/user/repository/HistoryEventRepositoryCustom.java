package com.idle.togeduck.domain.user.repository;

import java.util.List;

import com.idle.togeduck.domain.user.dto.HistoryEventResponseDto;

public interface HistoryEventRepositoryCustom {
	List<HistoryEventResponseDto> findEvents(Long historyId);
}
