package com.idle.togeduck.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.chat.dto.TradeResponseDto;
import com.idle.togeduck.domain.chat.entity.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	Slice<TradeResponseDto> findSliceByEventId(Long eventId, Pageable pageable);
}
