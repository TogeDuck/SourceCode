package com.idle.togeduck.domain.celebrity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idle.togeduck.domain.celebrity.entity.CelebrityUser;

public interface CelebrityUserRepository extends JpaRepository<CelebrityUser, Long> {

	@Query("SELECT cu FROM CelebrityUser cu WHERE cu.user.id = :userId and cu.celebrity.id = :celebrityId")
	Optional<CelebrityUser> findByUserId(Long userId, Long celebrityId);

	@Query("SELECT COUNT(cu) FROM CelebrityUser cu WHERE cu.celebrity.id = :celebrityId and cu.deleted = false")
	Long countByCelebrityId(Long celebrityId);
}
