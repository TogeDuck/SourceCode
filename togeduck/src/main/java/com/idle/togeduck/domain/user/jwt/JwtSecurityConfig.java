package com.idle.togeduck.domain.user.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// 직접 만든 JwtProvider 와 JwtFiler 를 SecurityConfig 에 적용할 때 사용
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtProvider jwtProvider;

	// JwtProvider 를 주입받아서 JwtFiler 를 통해 Security 로직에 필터를 등록
	@Override
	public void configure(HttpSecurity http) {

		JwtFilter customFiler = new JwtFilter(jwtProvider);

		http.addFilterBefore(customFiler, UsernamePasswordAuthenticationFilter.class);
	}
}
