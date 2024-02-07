package com.idle.togeduck.domain.user.repository;

import static com.idle.togeduck.domain.event.entity.QEvent.*;
import static com.idle.togeduck.domain.user.entity.QHistoryEvent.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.user.dto.HistoryEventResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class HistoryEventRepositoryCustomImpl implements HistoryEventRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HistoryEventResponseDto> findEvents(Long historyId) {
		return jpaQueryFactory
			.select(Projections.constructor(HistoryEventResponseDto.class,
				event.id,
				event.cafe.name,
				event.cafe.latitude,
				event.cafe.longitude))
			.from(historyEvent)
			.leftJoin(historyEvent.event, event)
			.where(historyEvent.history.id.eq(historyId)).fetch();
	}
}
