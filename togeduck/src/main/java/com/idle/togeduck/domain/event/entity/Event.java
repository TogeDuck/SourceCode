package com.idle.togeduck.domain.event.entity;

import java.time.LocalDate;

import com.idle.togeduck.domain.BaseEntity;
import com.idle.togeduck.domain.celebrity.entity.Celebrity;

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
public class Event extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "event_id")
	private Long id;

	private String name;

	private String content;

	private String image;

	private LocalDate startDate;

	private LocalDate endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "celebrity_id")
	private Celebrity celebrity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cafe_id")
	private Cafe cafe;

}
