package com.idle.togeduck.domain.celebrity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CelebrityService {

	private final CelebrityRepository celebrityRepository;

	public List<Celebrity> getAllCelebrity(String name, String nickname) {

		return celebrityRepository.findAllCelebrity(name, nickname);
	}
}
