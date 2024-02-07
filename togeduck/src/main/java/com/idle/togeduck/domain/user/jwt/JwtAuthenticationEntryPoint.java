package com.idle.togeduck.domain.user.jwt;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

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
		log.info(request.getAttribute("exception").toString());
		String code = (String)request.getAttribute("exception");
		// BaseException e = (BaseException)request.getAttribute("exeception");
		log.info("commence");
		if (code.equals("USER-006")) {
			log.info("entry");
			setResponse(response, ErrorCode.TOKEN_EXPIRED);
		}
	}

	// @Override
	// public void commence(HttpServletRequest request, HttpServletResponse response,
	// 	AuthenticationException authException) throws IOException, ServletException {
	//
	// 	log.info("확인");
	//
	// 	BaseException e = (BaseException)request.getAttribute("error");
	//
	// 	if (e != null) {
	// 		setResponse(response, e);
	// 	} else {
	// 		setResponse(response, new BaseException(ErrorCode.INTERNAL_SERVER_ERROR));
	// 	}
	//
	// 	// setResponse(response, new BaseException(ErrorCode.UNAUTHORIZED));
	// }
	//
	private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		log.info("만료");

		response.setStatus(HttpStatus.SC_UNAUTHORIZED);
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
