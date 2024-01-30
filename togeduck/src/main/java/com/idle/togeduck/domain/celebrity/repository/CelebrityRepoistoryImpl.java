package com.idle.togeduck.domain.celebrity.repository;

import static com.idle.togeduck.domain.celebrity.entity.QCelebrity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CelebrityRepoistoryImpl implements CelebrityRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Celebrity> findAllCelebrity(String name, String nickname) {
		return jpaQueryFactory
			.selectFrom(celebrity)
			.where(eqName(name),
				eqNickname(nickname))
			.fetch();
	}

	private BooleanExpression eqName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		return celebrity.name.eq(name);
	}

	private BooleanExpression eqNickname(String nickname) {
		if (StringUtils.isEmpty(nickname)) {
			return null;
		}
		return celebrity.nickname.eq(nickname);
	}
}
