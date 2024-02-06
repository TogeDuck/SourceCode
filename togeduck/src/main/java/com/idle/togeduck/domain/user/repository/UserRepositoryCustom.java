package com.idle.togeduck.domain.user.repository;

import com.idle.togeduck.domain.user.entity.User;

public interface UserRepositoryCustom {
	// List<User> findBySocialType(SocialType socialType);

	Long findBySocialId(String socialId);

	User findUserBySocialId(String socialId);
}
