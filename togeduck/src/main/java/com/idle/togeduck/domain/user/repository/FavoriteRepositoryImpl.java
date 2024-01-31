package com.idle.togeduck.domain.user.repository;

import static com.idle.togeduck.domain.user.entity.QFavorite.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.user.dto.FavoriteRequestDto;
import com.idle.togeduck.domain.user.entity.Favorite;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Favorite> findAllFavoriteByUserId(FavoriteRequestDto favoriteRequestDto) { // 내 즐겨찾기 연예인 목록
		return jpaQueryFactory
			.selectFrom(favorite)
			.where(favorite.user.id.eq(favoriteRequestDto.userId())
				.and(favorite.delCheck.eq(0)))
			.fetch();
	}

	@Override
	public Long findFavoriteByDelCheck(FavoriteRequestDto favoriteRequestDto) { // 즐겨찾기 여부 확인
		return jpaQueryFactory
			.select(favorite.user.count())
			.from(favorite)
			.where(favorite.user.id.eq(favoriteRequestDto.userId())
				.and(favorite.celebrity.id.eq(favoriteRequestDto.celebrityId())
					.and(favorite.delCheck.eq(1))))
			.fetchOne();
	}

	@Override
	public Long findFavorite(FavoriteRequestDto favoriteRequestDto) { // 즐겨찾기 추가
		return jpaQueryFactory
			.select(favorite.user.count())
			.from(favorite)
			.where(favorite.user.id.eq(favoriteRequestDto.userId())
				.and(favorite.celebrity.id.eq(favoriteRequestDto.celebrityId())))
			.fetchOne();
	}

	public void updateFavoriteByDelCheck(FavoriteRequestDto favoriteRequestDto, int value) { // 즐겨찾기 취소, 재추가
		jpaQueryFactory
			.update(favorite)
			.where(favorite.user.id.eq(favoriteRequestDto.userId())
				.and(favorite.celebrity.id.eq(favoriteRequestDto.celebrityId())))
			.set(favorite.delCheck, value)
			.execute();
	}
}
