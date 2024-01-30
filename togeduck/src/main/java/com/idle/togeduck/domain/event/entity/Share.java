package com.idle.togeduck.domain.event.entity;

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
@NoArgsConstructor
@AllArgsConstructor
public class Share extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "share_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String title;

	private String content;

	private String image;

	private Integer duration;

	public void updateShare(String originalFilename, String title, String content, Integer duration) {
		this.image = originalFilename;
		this.title = title;
		this.content = content;
		this.duration = duration;
	}
}
