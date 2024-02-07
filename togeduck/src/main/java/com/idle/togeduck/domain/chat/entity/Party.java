package com.idle.togeduck.domain.chat.entity;

import java.time.LocalDateTime;

import com.idle.togeduck.domain.event.entity.Event;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Party extends Quest {

	private String title;

	private Integer maximum;

	private Integer duration;

	private LocalDateTime expiredAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_id")
	private Event destination;

	public void updateParty(Event destination, String title, int maximum, int duration) {
		this.destination = destination;
		this.title = title;
		this.maximum = maximum;
		this.duration = duration;
	}
}
