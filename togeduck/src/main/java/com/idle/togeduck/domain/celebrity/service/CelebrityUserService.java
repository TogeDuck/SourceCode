package com.idle.togeduck.domain.celebrity.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.celebrity.dto.LocationRequestDto;
import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.idle.togeduck.domain.celebrity.entity.CelebrityUser;
import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;
import com.idle.togeduck.domain.celebrity.repository.CelebrityUserRepository;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CelebrityUserService {

	private final CelebrityRepository celebrityRepository;
	private final CelebrityUserRepository celebrityUserRepository;
	private final UserRepository userRepository;

	public void join(Long celebrityId, LocationRequestDto locationDto, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
		Celebrity celebrity = celebrityRepository.findById(celebrityId)
			.orElseThrow(() -> new BaseException(ErrorCode.CELEBRITY_NOT_FOUND));
		celebrityUserRepository.findByUserId(userId, celebrityId)
			.ifPresentOrElse(cu -> {
				cu.updateDeleted(false);
				cu.updateLocation(locationDto.latitude(), locationDto.longitude());
			}, () -> celebrityUserRepository.save(CelebrityUser.builder()
				.user(user)
				.celebrity(celebrity)
				.latitude(locationDto.latitude())
				.longitude(locationDto.longitude())
				.build()));
	}

	public void message(Long celebrityId, LocationRequestDto locationDto, Long userId) {
		celebrityUserRepository.findByUserId(userId, celebrityId)
			.ifPresentOrElse(cu ->
					cu.updateLocation(locationDto.latitude(), locationDto.longitude()),
				() -> {
					throw new BaseException(ErrorCode.CELEBRITY_USER_NOT_FOUND);
				});
	}

	public void leave(Long celebrityId, LocationRequestDto locationDto, Long userId) {
		celebrityUserRepository.findByUserId(userId, celebrityId)
			.ifPresentOrElse(cu ->
					cu.updateDeleted(true),
				() -> {
					throw new BaseException(ErrorCode.CELEBRITY_USER_NOT_FOUND);
				});
	}

	public Long count(Long celebrityId) {
		return celebrityUserRepository.countByCelebrityId(celebrityId);
	}
}
