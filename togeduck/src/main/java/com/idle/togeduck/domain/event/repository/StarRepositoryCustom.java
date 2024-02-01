package com.idle.togeduck.domain.event.repository;

import java.util.List;

import com.idle.togeduck.domain.event.dto.EventResponseDto;

public interface StarRepositoryCustom {
	List<EventResponseDto> findEventListByUserId(Long userId);

}
