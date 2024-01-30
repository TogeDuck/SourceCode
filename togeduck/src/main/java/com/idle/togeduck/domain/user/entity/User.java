package com.idle.togeduck.domain.user.entity;

import com.idle.togeduck.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	private String socialId;

	public void updateUser(String socialId, SocialType socialType) {
		this.socialId = socialId;
		this.socialType = socialType;
	}
}
