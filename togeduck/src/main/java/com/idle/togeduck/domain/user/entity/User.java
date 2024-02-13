package com.idle.togeduck.domain.user.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.idle.togeduck.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_id = ?")
public class User extends BaseEntity implements UserDetails {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private Authority authority;

	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	private String socialId;

	private String deviceToken;

	private boolean deleted = Boolean.FALSE;
	// private boolean deleted = Boolean.FALSE; // 삭제 여부 기본값 false

	public void updateUser(Authority authority, String socialId, SocialType socialType) {
		this.authority = authority;
		this.socialId = socialId;
		this.socialType = socialType;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(() -> {
			return String.valueOf(authority);
		});
		return collectors;
	}

	@Override
	public String getUsername() {
		return socialId;
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
