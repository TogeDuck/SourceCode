package com.idle.togeduck.domain.event.entity;

import com.idle.togeduck.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Team extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "team_id")
	private Long id;

	private String name;
}
