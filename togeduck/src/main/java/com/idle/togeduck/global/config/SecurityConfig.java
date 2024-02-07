package com.idle.togeduck.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.idle.togeduck.domain.user.jwt.CustomAuthenticaftionProvider;
import com.idle.togeduck.domain.user.jwt.JwtAccessDeniedHandler;
import com.idle.togeduck.domain.user.jwt.JwtAuthenticationEntryPoint;
import com.idle.togeduck.domain.user.jwt.JwtFilter;
import com.idle.togeduck.domain.user.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig { // 스프링 시큐리티에 필요한 설정

	private final JwtProvider jwtProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// CSRF 설정 Disable
		http
			.csrf(AbstractHttpConfigurer::disable)

			.addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)

			.headers((headers) ->
				headers.frameOptions(
					HeadersConfigurer.FrameOptionsConfig::sameOrigin))

			// exception handling 할 때 우리가 만든 클래스를 추가
			.exceptionHandling((authenticationEntryPoint) ->
				authenticationEntryPoint.authenticationEntryPoint(jwtAuthenticationEntryPoint)
					.accessDeniedHandler(jwtAccessDeniedHandler))

			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests
					.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.requestMatchers("/auth/**").permitAll()
					.requestMatchers("/ws-stomp/**").permitAll()
					.anyRequest().authenticated()
			);

		return http.build();
	}

	// @Bean
	// public PasswordEncoder passwordEncoder() {
	// 	return new BCryptPasswordEncoder();
	// }

	@Bean
	public AuthenticationManager authenticationManager(CustomAuthenticaftionProvider authenticationProvider) {
		return authentication -> authenticationProvider.authenticate(authentication);
	}
}
