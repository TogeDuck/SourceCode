package com.idle.togeduck.domain.event.repository;

import static com.idle.togeduck.domain.event.entity.QReview.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.event.dto.ReviewResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<ReviewResponseDto> findSliceByEventId(Long eventId, Long userId, Pageable pageable) {
		List<ReviewResponseDto> fetch = jpaQueryFactory
			.select(Projections.constructor(ReviewResponseDto.class, review.id, review.content,
				isMine(userId), review.image))
			.from(review)
			.where(review.event.id.eq(eventId))
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

	private BooleanExpression isMine(Long userId) {
		return review.user.id.eq(userId);
	}
}
