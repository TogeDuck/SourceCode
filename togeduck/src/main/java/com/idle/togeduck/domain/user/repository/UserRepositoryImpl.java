package com.idle.togeduck.domain.user.repository;

import static com.idle.togeduck.domain.user.entity.QUser.*;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	public Long updateDeleted(String socialId) {
		return jpaQueryFactory
			.update(user)
			.set(user.deleted, Boolean.FALSE)
			.where(user.socialId.eq(socialId))
			.execute();
	}

	public Long isDeleted(String socialId) {
		return jpaQueryFactory
			.select(user.socialId.count())
			.from(user)
			.where(user.socialId.eq(socialId).and(user.deleted.eq(Boolean.FALSE)))
			.fetchOne();
	}

	public Long findBySocialId(String socialId) {

		// return 값이 1이면 기존 게스트, 0이면 새로운 게스트
		return jpaQueryFactory
			.select(user.socialId.count())
			.from(user)
			.where(user.socialId.eq(socialId).and(user.deleted.eq(Boolean.FALSE)))
			.fetchOne();
	}

	@Override
	public User findUserBySocialId(String socialId) {
		return jpaQueryFactory
			.selectFrom(user)
			.where(user.socialId.eq(socialId).and(user.deleted.eq(Boolean.FALSE)))
			.fetchOne();
	}
}
