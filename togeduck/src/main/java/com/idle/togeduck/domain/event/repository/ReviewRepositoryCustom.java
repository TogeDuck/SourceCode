package com.idle.togeduck.domain.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.idle.togeduck.domain.event.dto.ReviewResponseDto;

public interface ReviewRepositoryCustom {
	Slice<ReviewResponseDto> findSliceByEventId(Long eventId, Long userId, Pageable pageable);
}
