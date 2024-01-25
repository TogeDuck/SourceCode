package com.idle.togeduck.domain.chat.entity;

import com.idle.togeduck.domain.event.entity.Event;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Party extends Chat {

	private String name;

	private String content;

	private Integer maximum;

	private Integer duration;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_id")
	private Event destination;

}
