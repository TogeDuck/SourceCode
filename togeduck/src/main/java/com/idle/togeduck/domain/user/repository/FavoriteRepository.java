package com.idle.togeduck.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.user.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {

}
