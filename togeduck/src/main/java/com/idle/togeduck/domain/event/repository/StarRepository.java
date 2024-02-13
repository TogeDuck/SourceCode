package com.idle.togeduck.domain.event.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.user.entity.Star;

public interface StarRepository extends JpaRepository<Star, Long>, StarRepositoryCustom {

	Optional<Star> findByUserIdAndEventId(Long userId, Long eventId);

	Optional<Star> findByEventId(Long eventId);

}
