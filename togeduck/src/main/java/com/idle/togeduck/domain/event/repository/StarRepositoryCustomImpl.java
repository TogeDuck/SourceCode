package com.idle.togeduck.domain.event.repository;

import static com.idle.togeduck.domain.event.entity.QCafe.*;
import static com.idle.togeduck.domain.event.entity.QEvent.*;
import static com.idle.togeduck.domain.user.entity.QHistoryEvent.*;
import static com.idle.togeduck.domain.user.entity.QStar.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.event.dto.JoinEventResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StarRepositoryCustomImpl implements StarRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<JoinEventResponseDto> findEventListByUserId(Long userId) {

		return jpaQueryFactory
			.select(Projections.constructor(JoinEventResponseDto.class,
				event.id,
				event.cafe.name,
				event.image1,
				event.image2,
				event.image3,
				event.startDate,
				event.endDate,
				event.cafe.latitude,
				event.cafe.longitude,
				Expressions.asBoolean(true),
				JPAExpressions.selectOne()
					.from(historyEvent)
					.where(historyEvent.event.id.eq(event.id).and(historyEvent.history.user.id.eq(userId)))
					.exists()))
			.from(event)
			.leftJoin(event.cafe, cafe)
			.where(event.id.in(
				JPAExpressions
					.select(star.event.id)
					.from(star)
					.where(star.user.id.eq(userId))
			))
			.fetch();
	}
}
