package com.idle.togeduck.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.idle.togeduck.domain.chat.dto.PartyResponseDto;

public interface PartyRepositoryCustom {
	Slice<PartyResponseDto> findPartyListByEventId(Long eventId, Pageable pageable);

	Long findUserCountByPartyId(Long partyId);
}
