package com.idle.togeduck.domain.user.jwt;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		setResponse(response, new BaseException(ErrorCode.UNAUTHORIZED_USER));
	}

	private void setResponse(HttpServletResponse response, BaseException e) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(e.getStatus());

		log.info("권한 없음");
		response.getWriter().write(objectMapper.writeValueAsString(new BaseException(e.getErrorCode())));
	}
}