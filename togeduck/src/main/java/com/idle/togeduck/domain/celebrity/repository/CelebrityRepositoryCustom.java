package com.idle.togeduck.domain.celebrity.repository;

import java.util.List;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;

public interface CelebrityRepositoryCustom {
	CelebrityResponseDto findCelebrity(Long id);

	List<CelebrityResponseDto> findAllCelebrity(String name, String nickname, String teamName);
}
