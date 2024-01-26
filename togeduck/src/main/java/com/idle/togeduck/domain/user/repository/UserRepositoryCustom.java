package com.idle.togeduck.domain.user.repository;

import java.util.List;
import java.util.Optional;

import com.idle.togeduck.domain.user.entity.SocialType;
import com.idle.togeduck.domain.user.entity.User;

public interface UserRepositoryCustom {
	List<User> findBySocialType(SocialType socialType);
}
