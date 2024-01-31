package com.idle.togeduck.domain.chat.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Trade extends Chat {

	private String image;

	private String content;

	private Integer duration;

	public void updateTrade(String originalFilename, String content, int duration) {
		this.image = originalFilename;
		this.content = content;
		this.duration = duration;
	}
}
