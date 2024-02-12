package com.idle.togeduck.domain.user.serivce;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.idle.togeduck.domain.user.dto.GoogleLoginResponseDto;
import com.idle.togeduck.domain.user.dto.GoogleRequestAccessTokenDto;
import com.idle.togeduck.domain.user.dto.SocialAuthResponseDto;
import com.idle.togeduck.domain.user.dto.SocialUserResponseDto;
import com.idle.togeduck.domain.user.entity.SocialType;
import com.idle.togeduck.domain.user.feign.google.GoogleAuthApi;
import com.idle.togeduck.domain.user.feign.google.GoogleUserApi;
import com.idle.togeduck.domain.user.util.GsonLocalDateTimeAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@ImportAutoConfiguration({FeignAutoConfiguration.class})
// @Qualifier("googleLogin")
public class GoogleLoginServiceImpl implements SocialLoginService {

	private final GoogleAuthApi googleAuthApi;
	private final GoogleUserApi googleUserApi;

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String googleAppKey;
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String googleAppSecret;
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String googleRedirectUri;
	@Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
	private String googleGrantType;

	@Override
	public SocialType getServiceName() {
		return SocialType.GOOGLE;
	}

	@Override
	public SocialAuthResponseDto getAccessToken(String authorizationCode) {
		ResponseEntity<?> response = googleAuthApi.getAccessToken(
			GoogleRequestAccessTokenDto.builder()
				.code(authorizationCode)
				.clientId(googleAppKey)
				.clientSecret(googleAppSecret)
				.redirectUri(googleRedirectUri)
				.grantType(googleGrantType)
				.build()
		);

		return new Gson()
			.fromJson(
				response.getBody().toString(),
				SocialAuthResponseDto.class
			);
	}

	@Override
	public SocialUserResponseDto getUserInfo(String accessToken) {
		ResponseEntity<?> response = googleUserApi.getUserInfo(accessToken);

		String jsonString = response.getBody().toString();

		Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();

		GoogleLoginResponseDto googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponseDto.class);

		return SocialUserResponseDto.builder()
			.socialId(googleLoginResponse.getId()) // getEmail()
			.build();
	}
}

