package com.idle.togeduck.domain.celebrity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CelebrityService {

	private final CelebrityRepository celebrityRepository;

	public List<CelebrityResponseDto> getAllCelebrity(String name, String nickname, String teamName) {

		return celebrityRepository.findAllCelebrity(name, nickname, teamName);
	}
}
