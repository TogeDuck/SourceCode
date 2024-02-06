package com.idle.togeduck.domain.celebrity.repository;

import java.util.List;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;

public interface CelebrityRepositoryCustom {
	List<CelebrityResponseDto> findAllCelebrity(String keyword);
}
