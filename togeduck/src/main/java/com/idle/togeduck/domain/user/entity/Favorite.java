package com.idle.togeduck.domain.user.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "favorite_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "celebrity_id")
	private Celebrity celebrity;

	private Integer delCheck;

	public void updateFavorite(Integer delCheck) {
		this.delCheck = delCheck;
	}
}
