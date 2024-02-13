package com.idle.togeduck.domain.user.serivce;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.user.dto.LoginRequestDto;
import com.idle.togeduck.domain.user.dto.SocialAuthResponseDto;
import com.idle.togeduck.domain.user.dto.SocialUserResponseDto;
import com.idle.togeduck.domain.user.dto.TokenDto;
import com.idle.togeduck.domain.user.dto.TokenRequestDto;
import com.idle.togeduck.domain.user.dto.UserRequestDto;
import com.idle.togeduck.domain.user.entity.Authority;
import com.idle.togeduck.domain.user.entity.SocialType;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.jwt.JwtProvider;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

	// private final GoogleLoginServiceImpl googleLoginService;

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;
	private final RedisService redisService;
	private final UserRepository userRepository;

	private final List<SocialLoginService> loginServices;

	// 소셜 타입 (GUEST, GOOGLE, KAKAO, NAVER) 확인
	private SocialLoginService getLoginService(SocialType socialType) {
		for (SocialLoginService loginService : loginServices) {
			if (socialType.equals(loginService.getServiceName())) {
				return loginService;
			}
		}
		return new GuestLoginServiceImpl();
	}

	// 회원가입
	private void join(UserRequestDto userRequestDto) {
		userRepository.save(
			User.builder()
				.socialId(userRequestDto.socialId()) // 소셜 아이디
				.socialType(userRequestDto.socialType()) // 소셜 타입
				.authority(Authority.ROLE_USER) // 권한
				.build()
		);
	}

	// public void socialLogin(String code) {
	// 	SocialAuthResponseDto socialAuthResponseDto = googleLoginService.getAccessToken(code);
	// 	SocialUserResponseDto socialUserResponseDto = googleLoginService.getUserInfo(
	// 		socialAuthResponseDto.getAccess_token());
	// 	log.info(socialUserResponseDto.socialId());
	// }

	// 로그인
	@Transactional
	public TokenDto login(LoginRequestDto loginRequestDto) {

		String socialId;

		// 소셜 타입 확인에 맞는 Service 사용
		SocialLoginService loginService = getLoginService(loginRequestDto.socialType());

		if (loginService.getServiceName().equals(SocialType.GUEST)) { // GUEST
			socialId = loginRequestDto.code(); // UID
		} else { // GOOGLE, KAKAO ,NAVER
			SocialAuthResponseDto socialAuthResponseDto = loginService.getAccessToken(
				loginRequestDto.code()); // 인증코드로 토큰 가져오기
			SocialUserResponseDto socialUserResponseDto = loginService.getUserInfo(
				socialAuthResponseDto.getAccess_token()); // 토큰으로 유저 정보 가져오기
			socialId = socialUserResponseDto.socialId();
		}

		if (userRepository.findBySocialId(socialId) == 0) {
			// || userRepository.isDeleted(socialId) == 0) { // DB 에 정보 없으면 회원가입
			/*
			 	GUEST 인 경우에 재로그인 시 에러 보낼 코드 작성 필요
			 */
			join(
				UserRequestDto.builder()
					.socialId(socialId)
					.socialType(loginRequestDto.socialType())
					.build()
			);
		}

		User user = userRepository.findUserBySocialId(socialId); // 유저 정보

		// 1. Login 정보를 기반으로 AuthenticationToken 생성
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(user.getSocialId(), "", user.getAuthorities());

		// 2. 실제로 검증이 이루어지는 부분
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		// 3. 인증 정보를 기반으로 JWT 토큰 생성
		TokenDto tokenDto = jwtProvider.createTokenDtoWithUserId(user.getId(), authentication);

		// 4. RefreshToken redis 저장
		String refreshToken = tokenDto.getRefreshToken();

		redisService.setValues(user.getSocialId(), refreshToken);

		return tokenDto;
	}

	@Transactional
	public TokenDto reissue(TokenRequestDto tokenRequestDto, User user) {

		// 1. Refresh Token 검증
		if (!jwtProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
		}

		// 2. Access Token 에서 user ID 가져오기
		Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());

		// 3. 저장소에서 user ID 를 기반으로 refresh Token 값 가져옴
		String refreshToken = redisService.getValues(authentication.getName());
		if (refreshToken == null) {
			throw new RuntimeException("로그아웃 된 사용자입니다.");
		}

		// 4. Refresh Token 일치하는지 검사
		if (!refreshToken.equals(tokenRequestDto.getRefreshToken())) {
			throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
		}

		// 5. 새로운 토큰 생성
		TokenDto tokenDto = jwtProvider.createTokenDtoWithUserId(user.getId(), authentication);

		// 6. 저장소 정보 업데이트
		redisService.setValues(authentication.getName(), tokenDto.getRefreshToken());

		// 토큰 발급
		return tokenDto;
	}

	@Transactional
	public void logout(TokenRequestDto tokenRequestDto) {
		if (!jwtProvider.validateToken(tokenRequestDto.getAccessToken())) {
			throw new BaseException(ErrorCode.TOKEN_NOT_EXISTS);
		}

		Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());
		Long expiration = jwtProvider.getExpiration(tokenRequestDto.getAccessToken());

		redisService.delValues(authentication.getName());
		redisService.setBlackList(tokenRequestDto.getAccessToken(), "access_token", expiration);
	}

	@Transactional
	public void withdrawal(User user) {
		userRepository.delete(user);
	}
}
