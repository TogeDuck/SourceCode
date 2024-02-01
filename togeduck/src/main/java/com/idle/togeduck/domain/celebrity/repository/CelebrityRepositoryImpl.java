package com.idle.togeduck.domain.celebrity.repository;

import static com.idle.togeduck.domain.celebrity.entity.QCelebrity.*;
import static com.idle.togeduck.domain.event.entity.QTeam.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CelebrityRepositoryImpl implements CelebrityRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public CelebrityResponseDto findCelebrity(Long id) {
		Celebrity celeb = jpaQueryFactory
			.selectFrom(celebrity)
			.where(celebrity.id.eq(id))
			.join(celebrity.team, team).fetchJoin()
			.fetchOne();

		return new CelebrityResponseDto(celeb.getId(), celeb.getName(), celeb.getNickname(),
			celeb.getBirthday(),
			celeb.getImage(), celeb.getTeam().getName(), celeb.getTeam().getColor());
	}

	@Override
	public List<CelebrityResponseDto> findAllCelebrity(String name, String nickname, String teamName) {
		List<Celebrity> celebrities = jpaQueryFactory
			.selectFrom(celebrity)
			.where(eqName(name),
				eqNickname(nickname),
				eqTeamName(teamName))
			.join(celebrity.team, team).fetchJoin()
			.fetch();

		List<CelebrityResponseDto> celebrityResponseDtos = new ArrayList<>();

		for (Celebrity celebrity : celebrities) {
			CelebrityResponseDto celebrityResponseDto
				= new CelebrityResponseDto(
				celebrity.getId(), celebrity.getName(), celebrity.getNickname(), celebrity.getBirthday(),
				celebrity.getImage(), celebrity.getTeam().getName(), celebrity.getTeam().getColor());
			celebrityResponseDtos.add(celebrityResponseDto);
		}
		return celebrityResponseDtos;
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

	private BooleanExpression eqTeamName(String teamName) {
		if (StringUtils.isEmpty(teamName)) {
			return null;
		}
		return celebrity.team.name.eq(teamName);
	}
}
