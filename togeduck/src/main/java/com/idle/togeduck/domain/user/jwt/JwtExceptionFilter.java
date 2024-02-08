package com.idle.togeduck.domain.user.jwt;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			//만료 에러
			log.info("base");
			request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED.getCode());
			System.out.println(((BaseException)e).getCode());
			throw e;
		}
		filterChain.doFilter(request, response);

	}
}