package com.idle.togeduck.domain.celebrity.repository;

import static com.idle.togeduck.domain.celebrity.entity.QCelebrity.*;
import static com.idle.togeduck.domain.event.entity.QTeam.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CelebrityRepositoryImpl implements CelebrityRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<CelebrityResponseDto> findAllCelebrity(String keyword) {
		List<Celebrity> celebrities = jpaQueryFactory
			.selectFrom(celebrity)
			.where(celebrity.name.eq(keyword).or(celebrity.nickname.eq(keyword)).or(celebrity.team.name.eq(keyword)))
			.join(celebrity.team, team).fetchJoin()
			.fetch();

		List<CelebrityResponseDto> celebrityResponseDtolist = new ArrayList<>();

		for (Celebrity celebrity : celebrities) {
			CelebrityResponseDto celebrityResponseDto
				= CelebrityResponseDto.builder()
				.id(celebrity.getId())
				.name(celebrity.getName())
				.nickname(celebrity.getNickname())
				.birthday(celebrity.getBirthday())
				.image(celebrity.getImage())
				.teamName(celebrity.getTeam().getName())
				.build();
			celebrityResponseDtolist.add(celebrityResponseDto);
		}
		return celebrityResponseDtolist;
	}
}
