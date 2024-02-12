package com.idle.togeduck.domain.user.repository;

import com.idle.togeduck.domain.user.entity.User;

public interface UserRepositoryCustom {

	Long updateDeleted(String socialId);

	Long isDeleted(String socialId);

	Long findBySocialId(String socialId);

	User findUserBySocialId(String socialId);
}
