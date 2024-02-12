package com.idle.togeduck.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialAuthResponseDto {
	private String access_token;
	private String token_type;
	private String expires_in;
	private String scope;
	private String id_token;
}
