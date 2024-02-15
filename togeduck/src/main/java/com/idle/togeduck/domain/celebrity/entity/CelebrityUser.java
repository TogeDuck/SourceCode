package com.idle.togeduck.domain.celebrity.entity;

import org.hibernate.annotations.SQLDelete;

import com.idle.togeduck.domain.BaseEntity;
import com.idle.togeduck.domain.user.entity.User;

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
@SQLDelete(sql = "UPDATE celebrity_user SET deleted = true WHERE celebrity_user_id = ?")
@NoArgsConstructor
@AllArgsConstructor
public class CelebrityUser extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "celebrity_user_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "celebrity_id")
	private Celebrity celebrity;

	private boolean deleted = Boolean.FALSE;

	private Double latitude;

	private Double longitude;

	public void updateLocation(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void updateDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
