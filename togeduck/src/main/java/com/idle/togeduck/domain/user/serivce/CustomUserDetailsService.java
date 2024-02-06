package com.idle.togeduck.domain.user.serivce;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String socialId) throws UsernameNotFoundException {

		User user = userRepository.findUserBySocialId(socialId);
		log.info("UserDetails의 User >> " + user.toString());
		if (user == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		return user;
	}
}
