package com.idle.togeduck.domain.user.repository;

import java.util.List;

import com.idle.togeduck.domain.user.dto.FavoriteRequestDto;
import com.idle.togeduck.domain.user.entity.Favorite;
import com.idle.togeduck.domain.user.entity.User;

public interface FavoriteRepositoryCustom {

	List<Favorite> findAllFavoriteByUserId(User user);

	Long findFavorite(FavoriteRequestDto favoriteRequestDto, Long userId);

	Long findFavoriteByDelCheck(FavoriteRequestDto favoriteRequestDto, Long userId);

	void updateFavoriteByDelCheck(FavoriteRequestDto favoriteRequestDto, Long userId, int value);

}
