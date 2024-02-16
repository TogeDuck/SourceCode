package com.idle.togeduck.domain.event.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.event.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
	Optional<Review> findByIdAndUserId(Long reviewId, Long userId);
}
