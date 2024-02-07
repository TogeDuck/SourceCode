package com.idle.togeduck.domain.chat.repository;

import static com.idle.togeduck.domain.chat.entity.QChat.*;
import static com.idle.togeduck.domain.chat.entity.QParty.*;
import static com.idle.togeduck.domain.chat.entity.QUserChat.*;
import static com.idle.togeduck.domain.event.entity.QCafe.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.chat.dto.PartyResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class PartyRepositoryImpl implements PartyRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<PartyResponseDto> findPartyListByEventId(Long userId, Long eventId, Pageable pageable) {

		List<PartyResponseDto> fetch = queryFactory
			.select(Projections.constructor(PartyResponseDto.class,
				party.id,
				party.title,
				party.destination.cafe.name,
				party.maximum,
				party.duration,
				JPAExpressions.select(userChat.count())
					.from(userChat)
					.where(userChat.chat.id.eq(
							JPAExpressions.select(chat.id)
								.from(chat)
								.where(chat.quest.id.eq(party.id))),
						userChat.deleted.eq(false)),
				party.createdAt,
				party.expiredAt,
				party.user.id.eq(userId)))
			.from(party)
			.leftJoin(party.destination.cafe, cafe)
			.where(party.event.id.eq(eventId), isActivated(LocalDateTime.now()))
			.orderBy(party.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		boolean hasNext = false;
		if (fetch.size() > pageable.getPageSize()) {
			fetch.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(fetch, pageable, hasNext);
	}

	@Override
	public Long findUserCountByPartyId(Long partyId) {
		return queryFactory
			.select(userChat.count())
			.from(userChat)
			.where(userChat.chat.id.eq(partyId))
			.fetchOne();
	}

	private BooleanExpression isActivated(LocalDateTime now) {
		return party.createdAt.before(now).and(party.expiredAt.after(now));
	}
}
