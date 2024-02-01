package com.idle.togeduck.domain.user.repository;

import java.util.List;

import com.idle.togeduck.domain.user.dto.FavoriteRequestDto;
import com.idle.togeduck.domain.user.entity.Favorite;

public interface FavoriteRepositoryCustom {

	List<Favorite> findAllFavoriteByUserId(FavoriteRequestDto favoriteRequestDto);

	Long findFavorite(FavoriteRequestDto favoriteRequestDto);

	Long findFavoriteByDelCheck(FavoriteRequestDto favoriteRequestDto);

	void updateFavoriteByDelCheck(FavoriteRequestDto favoriteRequestDto, int value);

}
