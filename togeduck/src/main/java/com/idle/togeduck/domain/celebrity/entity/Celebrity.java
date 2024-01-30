package com.idle.togeduck.domain.celebrity.entity;

import java.time.LocalDate;

import com.idle.togeduck.domain.BaseEntity;
import com.idle.togeduck.domain.event.entity.Team;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Celebrity extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "celebrity_id")
	private Long id;

	private String name;

	private String nickname;

	private String image;

	private LocalDate birthday;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;
}
