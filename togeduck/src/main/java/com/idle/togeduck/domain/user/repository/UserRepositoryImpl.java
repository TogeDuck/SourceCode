package com.idle.togeduck.domain.user.repository;

import static com.idle.togeduck.domain.user.entity.QUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.user.dto.UserRequestDto;
import com.idle.togeduck.domain.user.entity.SocialType;
import com.idle.togeduck.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<User> findBySocialType(SocialType socialType) {
		return jpaQueryFactory.selectFrom(user)
			.where(user.socialType.eq(socialType))
			.fetch();
	}

	public Long findBySocialId(UserRequestDto userRequestDto) {

		// return 값이 1이면 기존 게스트, 0이면 새로운 게스트
		return jpaQueryFactory
			.select(user.socialId.count())
			.from(user)
			.where(user.socialId.eq(userRequestDto.socialId()))
			.fetchOne();
	}

	@Override
	public User findUserBySocialId(String socialId) {
		return jpaQueryFactory
			.selectFrom(user)
			.where(user.socialId.eq(socialId))
			.fetchOne();
	}
}
