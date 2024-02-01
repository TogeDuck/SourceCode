package com.idle.togeduck.domain.celebrity.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CelebrityResponseDto {

	private Long id;
	private String name;
	private String nickname;
	private LocalDate birthday;
	private String image;
	private String teamName;
	private String teamColor;
}
