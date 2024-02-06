package com.idle.togeduck.domain.celebrity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.celebrity.dto.CelebrityRequestDto;
import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CelebrityService {

	private final CelebrityRepository celebrityRepository;

	public List<CelebrityResponseDto> getAllCelebrity(CelebrityRequestDto celebrityRequestDto) {

		// List<CelebrityResponseDto> celebrityResponseDtoList = new ArrayList<>();
		// celebrityResponseDtoList.addAll(celebrityRepository.findAllCelebrity(celebrityRequestDto.keyword()));

		return celebrityRepository.findAllCelebrity(celebrityRequestDto.keyword());
	}
}
