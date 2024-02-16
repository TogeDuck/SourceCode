package com.idle.togeduck.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idle.togeduck.domain.chat.dto.TradeResponseDto;
import com.idle.togeduck.domain.chat.entity.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	@Query("select new com.idle.togeduck.domain.chat.dto.TradeResponseDto("
		+ "t.id, t.content, t.image, t.duration, t.createdAt, t.expiredAt, t.user.id = :userId) "
		+ "from Trade t "
		+ "where t.event.id = :eventId "
		+ "and t.createdAt <= current_time and current_time <= t.expiredAt "
		+ "order by t.createdAt desc ")
	Slice<TradeResponseDto> findSliceByEventId(Long userId, Long eventId, Pageable pageable);

	@Query("select new com.idle.togeduck.domain.chat.dto.TradeResponseDto("
		+ "t.id, t.content, t.image, t.duration, t.createdAt, t.expiredAt, t.user.id = :userId) "
		+ "from Trade t "
		+ "where t.event.id = :eventId and t.user.id = :userId "
		+ "and t.createdAt < current_time and current_time < t.expiredAt "
		+ "order by t.createdAt desc ")
	Slice<TradeResponseDto> findSliceByEventIdAndUserId(Long userId, Long eventId, Pageable pageable);

	@Query("select t from Trade t join fetch t.user where t.id = :tradeId")
	Optional<Trade> findByIdWithUser(Long tradeId);
}
