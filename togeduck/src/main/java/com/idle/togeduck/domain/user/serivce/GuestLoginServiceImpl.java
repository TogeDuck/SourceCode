package com.idle.togeduck.domain.user.serivce;

import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.user.dto.SocialAuthResponseDto;
import com.idle.togeduck.domain.user.dto.SocialUserResponseDto;
import com.idle.togeduck.domain.user.entity.SocialType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuestLoginServiceImpl implements SocialLoginService {

	@Override
	public SocialType getServiceName() {
		return SocialType.GUEST;
	}

	@Override
	public SocialAuthResponseDto getAccessToken(String authorizationCode) {
		return null;
	}

	@Override
	public SocialUserResponseDto getUserInfo(String accessToken) {
		return null;
	}
}
