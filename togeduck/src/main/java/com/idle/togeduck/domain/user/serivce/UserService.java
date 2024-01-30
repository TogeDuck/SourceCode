package com.idle.togeduck.domain.user.serivce;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.user.dto.UserRequestDto;
import com.idle.togeduck.domain.user.dto.UserResponseDto;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public void insertUserBySocialId(UserRequestDto userRequestDto) {

		if (userRepository.findBySocialId(userRequestDto) == 0) {

			User requestUser = new User();

			requestUser.updateUser(userRequestDto.socialId(), userRequestDto.socialType());

			userRepository.save(requestUser);
		}
	}

	public UserResponseDto getUserBySocialId(String socialId) { // 유저 아이디로 유저 정보 받아오기

		User user = new User();
		user = userRepository.findUserBySocialId(socialId);

		return new UserResponseDto(user.getId(), user.getSocialId(), user.getSocialType());
	}
}
