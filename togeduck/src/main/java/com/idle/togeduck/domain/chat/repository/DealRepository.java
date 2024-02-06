package com.idle.togeduck.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idle.togeduck.domain.chat.entity.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {
	@Query("select d from Deal d join fetch d.target t join fetch t.user join fetch d.myTrade m join fetch m.user where d.id = :dealId")
	Optional<Deal> findByIdWithTarget(Long dealId);
}
