package com.idle.togeduck.domain.user.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.idle.togeduck.global.response.BaseException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer";

	private final JwtProvider jwtProvider;

	// 실제 필터링 로직
	// JWT 토큰의 인증 정보를 현재 쓰레드의 securityContext 에 저장하는 역할 수행
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		log.info("인증처리 시작");

		// 1. request Header 에서 토큰을 꺼내옴
		String jwt = resolveToken(request);

		log.info("토큰 권한 Security 저장 : " + jwt);

		// 2. validateToken 으로 토큰 유효성 검사
		try {
			if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
				// 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
				Authentication authentication = jwtProvider.getAuthentication(jwt);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (BaseException e) {
			log.info("확인");
			throw e;
		}
		filterChain.doFilter(request, response);
	}

	// Request Header 에서 토큰 정보를 꺼내오기
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.split(" ")[1].trim();
		}
		return null;
	}
}
