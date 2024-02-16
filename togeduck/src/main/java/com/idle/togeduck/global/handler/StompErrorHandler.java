package com.idle.togeduck.global.handler;

import java.nio.charset.StandardCharsets;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.idle.togeduck.global.response.BaseException;

@Component
public class StompErrorHandler extends StompSubProtocolErrorHandler {
	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
		//ex가 BaseException일 경우에만 처리
		if (ex instanceof BaseException) {
			return prepareErrorMessage((BaseException)ex);
		}
		return super.handleClientMessageProcessingError(clientMessage, ex);
	}

	private Message<byte[]> prepareErrorMessage(BaseException e) {

		String code = e.getCode();

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

		accessor.setMessage(String.valueOf(code));
		accessor.setLeaveMutable(true);

		return MessageBuilder.createMessage(code.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
	}
}
