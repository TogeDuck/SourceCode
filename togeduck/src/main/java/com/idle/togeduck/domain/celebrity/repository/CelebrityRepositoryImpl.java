package com.idle.togeduck.domain.celebrity.repository;

import static com.idle.togeduck.domain.celebrity.entity.QCelebrity.*;
import static com.idle.togeduck.domain.event.entity.QTeam.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CelebrityRepositoryImpl implements CelebrityRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Celebrity> findCelebrityByKeyword(String keyword) {

		return jpaQueryFactory
			.selectFrom(celebrity)
			.where(celebrity.name.eq(keyword).or(celebrity.nickname.eq(keyword)).or(celebrity.team.name.eq(keyword)))
			.join(celebrity.team, team).fetchJoin()
			.fetch();
	}
}
