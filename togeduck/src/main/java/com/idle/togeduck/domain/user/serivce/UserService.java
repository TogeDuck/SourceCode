package com.idle.togeduck.domain.user.serivce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.celebrity.dto.CelebrityResponseDto;
import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;
import com.idle.togeduck.domain.user.dto.FavoriteRequestDto;
import com.idle.togeduck.domain.user.entity.Favorite;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.FavoriteRepository;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final CelebrityRepository celebrityRepository;
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;

	@Transactional
	public List<CelebrityResponseDto> getFavoriteCelebrity(FavoriteRequestDto favoriteRequestDto) { // 관심연예인 목록

		List<Favorite> favoriteList = favoriteRepository.findAllFavoriteByUserId(favoriteRequestDto);

		List<CelebrityResponseDto> celebritylist = new ArrayList<>();

		for (Favorite favorite : favoriteList) {
			celebritylist.add(celebrityRepository.findCelebrityById(favorite.getCelebrity().getId()));
		}
		return celebritylist;
	}

	@Transactional
	public void upsertFavorite(FavoriteRequestDto favoriteRequestDto) { // 관심 연예인 추가 및 삭제

		Celebrity celebrity = celebrityRepository.findById(favoriteRequestDto.celebrityId())
			.orElseThrow(() -> new BaseException(ErrorCode.CELEBRITY_NOT_FOUND));
		User user = userRepository.findById(favoriteRequestDto.userId())
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

		if (favoriteRepository.findFavorite(favoriteRequestDto) == 0) { // 관심 연예인 디비에 없음
			favoriteRepository.save(Favorite.builder()
				.user(user)
				.celebrity(celebrity)
				.build());
		} else { // 관심 연예인 디비에 있음
			if (favoriteRepository.findFavoriteByDelCheck(favoriteRequestDto) == 0) { // delcheck == 0 이면 삭제
				favoriteRepository.updateFavoriteByDelCheck(favoriteRequestDto, 1);
			} else { // delcheck == 1 (삭제 처리)이면 다시 추가
				favoriteRepository.updateFavoriteByDelCheck(favoriteRequestDto, 0);
			}
		}
	}

	// public UserResponseDto getUserBySocialId(String socialId) { // 유저 아이디로 유저 정보 받아오기
	// 	User user = userRepository.findUserBySocialId(socialId);
	// 	return new UserResponseDto(user.getId(), user.getSocialId(), user.getSocialType());
	// }
}
