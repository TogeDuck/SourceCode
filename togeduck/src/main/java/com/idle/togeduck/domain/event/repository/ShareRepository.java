package com.idle.togeduck.domain.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.idle.togeduck.domain.event.dto.ShareRespondDto;
import com.idle.togeduck.domain.event.entity.Share;

public interface ShareRepository extends JpaRepository<Share, Long> {
	Slice<ShareRespondDto> findSliceByEventId(Long eventId, Pageable pageable);
}
