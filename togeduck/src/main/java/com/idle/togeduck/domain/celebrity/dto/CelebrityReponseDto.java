package com.idle.togeduck.domain.celebrity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CelebrityReponseDto {

	private String celebrityId;
	private String celebrityName;
	private String celebrityNickname;
	private String celebrityBirthday;
	private String celebrityImage;
	private String celebrityTeamName;
	private String celebrityTeamColor;
}
