package com.idle.togeduck.domain.event.repository;

import java.util.List;

import com.idle.togeduck.domain.event.dto.JoinEventResponseDto;

public interface StarRepositoryCustom {
	List<JoinEventResponseDto> findEventListByUserId(Long userId);

}
