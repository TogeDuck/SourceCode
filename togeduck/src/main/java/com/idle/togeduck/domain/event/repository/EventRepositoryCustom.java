package com.idle.togeduck.domain.event.repository;

import java.time.LocalDate;
import java.util.List;

import com.idle.togeduck.domain.event.dto.JoinEventResponseDto;

public interface EventRepositoryCustom {
	List<JoinEventResponseDto> findEventList(Long celebrityId, LocalDate startDate, LocalDate endDate, Long userId);
}
