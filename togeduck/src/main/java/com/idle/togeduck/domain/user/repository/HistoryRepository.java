package com.idle.togeduck.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idle.togeduck.domain.user.dto.HistoryResponseDto;
import com.idle.togeduck.domain.user.entity.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

	@Query("select new com.idle.togeduck.domain.user.dto.HistoryResponseDto("
		+ "h.id, h.name, h.date) "
		+ "from History h "
		+ "where h.celebrityId = :celebrityId and h.user.id = :userId and length(h.route)> 90 "
		+ "order by h.createdAt desc")
	List<HistoryResponseDto> findMyHistory(Long celebrityId, Long userId);
}
