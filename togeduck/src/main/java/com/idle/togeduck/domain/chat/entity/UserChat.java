package com.idle.togeduck.domain.chat.entity;

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
@SQLDelete(sql = "UPDATE user_chat SET deleted = true WHERE user_chat_id = ?")
@NoArgsConstructor
@AllArgsConstructor
public class UserChat extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "user_chat_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_id")
	private Chat chat;

	private boolean deleted = Boolean.FALSE;

	public void setDeleted(boolean b) {
		this.deleted = b;
	}
}
