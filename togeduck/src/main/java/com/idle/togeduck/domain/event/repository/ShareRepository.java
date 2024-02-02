package com.idle.togeduck.domain.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idle.togeduck.domain.event.dto.ShareRespondDto;
import com.idle.togeduck.domain.event.entity.Share;

public interface ShareRepository extends JpaRepository<Share, Long> {
	@Query("select new com.idle.togeduck.domain.event.dto.ShareRespondDto("
		+ "s.id, s.image, s.title, s.content, s.duration, s.user.id = :userId) "
		+ "from Share s "
		+ "where s.event.id = :eventId "
		+ "order by s.createdAt desc")
	Slice<ShareRespondDto> findSliceByEventId(Long userId, Long eventId, Pageable pageable);
}
