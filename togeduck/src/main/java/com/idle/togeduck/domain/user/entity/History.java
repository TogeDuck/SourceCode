package com.idle.togeduck.domain.user.entity;

import java.time.LocalDate;

import com.idle.togeduck.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "history_id")
	private Long id;

	private String name;

	private LocalDate date;

	private String route;

	private Long celebrityId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public void updateName(String name) {
		this.name = name;
	}

	public void updateRoute(String route) {
		this.route = route;
	}
}
