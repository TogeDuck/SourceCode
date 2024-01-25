package com.idle.togeduck.domain.chat.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Trade extends Chat {

	private String image;

	private String content;

	private Integer duration;

}
