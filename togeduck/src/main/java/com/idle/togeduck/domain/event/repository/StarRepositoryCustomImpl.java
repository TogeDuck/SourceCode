package com.idle.togeduck.domain.event.repository;

import static com.idle.togeduck.domain.event.entity.QCafe.*;
import static com.idle.togeduck.domain.event.entity.QEvent.*;
import static com.idle.togeduck.domain.user.entity.QStar.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.event.dto.EventResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StarRepositoryCustomImpl implements StarRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<EventResponseDto> findEventListByUserId(Long userId) {

		return jpaQueryFactory
			.select(Projections.constructor(EventResponseDto.class, event.id, event.image, event.name, event.content,
				event.startDate, event.endDate, event.cafe.latitude, event.cafe.longitude))
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
