package com.idle.togeduck.domain.event.repository;

import static com.idle.togeduck.domain.event.entity.QCafe.*;
import static com.idle.togeduck.domain.event.entity.QEvent.*;
import static com.idle.togeduck.domain.user.entity.QHistoryEvent.*;
import static com.idle.togeduck.domain.user.entity.QStar.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.event.dto.JoinEventResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<JoinEventResponseDto> findEventList(Long celebrityId, LocalDate startDate, LocalDate endDate,
		Long userId) {
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
				JPAExpressions.selectOne()
					.from(star)
					.where(star.event.id.eq(event.id)
						.and(star.user.id.eq(userId)))
					.exists(),
				JPAExpressions.selectOne()
					.from(historyEvent)
					.where(historyEvent.event.id.eq(event.id).and(historyEvent.history.user.id.eq(userId)))
					.exists()
			))
			.from(event)
			.leftJoin(event.cafe, cafe)
			.where(event.startDate.between(startDate, endDate)
				.or(event.endDate.between(startDate, endDate))
				.and(event.celebrity.id.eq(celebrityId)))
			.fetch();
	}

	@Override
	public JoinEventResponseDto findByEventId(Long eventId, Long userId) {
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
				JPAExpressions.selectOne()
					.from(star)
					.where(star.event.id.eq(event.id)
						.and(star.user.id.eq(userId)))
					.exists(),
				JPAExpressions.selectOne()
					.from(historyEvent)
					.where(historyEvent.event.id.eq(event.id).and(historyEvent.history.user.id.eq(userId)))
					.exists()
			))
			.from(event)
			.leftJoin(event.cafe, cafe)
			.where(event.id.eq(eventId))
			.fetch().get(0);
	}

}
