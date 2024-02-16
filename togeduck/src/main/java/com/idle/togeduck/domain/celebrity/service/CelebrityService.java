package com.idle.togeduck.domain.celebrity.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CelebrityService {

	private final CelebrityRepository celebrityRepository;

	public List<CelebrityResponseDto> getCelebrityByKeyword(String keyword) {

		List<Celebrity> celebrities = celebrityRepository.findCelebrityByKeyword(keyword);

		List<CelebrityResponseDto> celebrityResponseDtolist = new ArrayList<>();
		for (Celebrity celebrity : celebrities) {
			CelebrityResponseDto celebrityResponseDto
				= CelebrityResponseDto.builder()
				.id(celebrity.getId())
				.name(celebrity.getName())
				.nickname(celebrity.getNickname())
				.birthday(celebrity.getBirthday())
				.image(celebrity.getImage())
				.teamName(celebrity.getTeam().getName())
				.build();
			celebrityResponseDtolist.add(celebrityResponseDto);
		}
		return celebrityResponseDtolist;
	}
}
