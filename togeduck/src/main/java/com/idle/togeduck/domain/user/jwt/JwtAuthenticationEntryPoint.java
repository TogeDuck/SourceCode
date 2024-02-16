package com.idle.togeduck.domain.user.jwt;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.togeduck.global.response.ErrorCode;
import com.idle.togeduck.global.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		String code = (String)request.getAttribute("exception");
		if (StringUtils.hasText(code) && code.equals("USER-006")) {
			setResponse(response, ErrorCode.TOKEN_EXPIRED);
		} else {
			setResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		response.setStatus(code.getStatus());
		response.getWriter()
			.write(objectMapper.writeValueAsString(new ErrorResponse(code.getStatus(), code.getCode())));
	}

	// @Override
	// public void commence(HttpServletRequest request, HttpServletResponse response,
	// 	AuthenticationException authException) throws IOException, ServletException {

	// String exception = (String)request.getAttribute("Exception");
	// response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	// response.setHeader(HttpHeaders.CONTENT_ENCODING, "UTF-8");
	// response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
	// response.getWriter().write(exception);
	// response.getWriter().flush();

	// response.getWriter().println(new BaseResponse<>(HttpServletResponse.SC_UNAUTHORIZED, exception, ""));

	// 유효한 자격증명을 제공하지 않고 접근하려 할 때 에러 401 (사용자 정보, 토큰 유효)
	// response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	// }
}
