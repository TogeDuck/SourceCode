package com.idle.togeduck.domain.user.serivce;

import org.springframework.stereotype.Service;

import com.idle.togeduck.domain.user.dto.SocialAuthResponseDto;
import com.idle.togeduck.domain.user.dto.SocialUserResponseDto;
import com.idle.togeduck.domain.user.entity.SocialType;

@Service
public interface SocialLoginService {
	SocialType getServiceName();

	SocialAuthResponseDto getAccessToken(String authorizationCode);

	SocialUserResponseDto getUserInfo(String accessToken);
}
