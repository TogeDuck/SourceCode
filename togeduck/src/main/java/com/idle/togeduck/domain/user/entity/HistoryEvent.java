package com.idle.togeduck.domain.user.entity;

import com.idle.togeduck.domain.BaseEntity;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.user.entity.History;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEvent extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "history_event_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "history_jd")
	private History history;
}
