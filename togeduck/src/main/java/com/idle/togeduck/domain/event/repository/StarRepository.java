package com.idle.togeduck.domain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.user.entity.Star;

public interface StarRepository extends JpaRepository<Star, Long>, StarRepositoryCustom {
}
