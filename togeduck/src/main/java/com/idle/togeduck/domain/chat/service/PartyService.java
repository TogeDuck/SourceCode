package com.idle.togeduck.domain.chat.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.chat.dto.PartyResponseDto;
import com.idle.togeduck.domain.chat.entity.Party;
import com.idle.togeduck.domain.chat.repository.PartyRepository;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.event.repository.EventRepository;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartyService {

	private final PartyRepository partyRepository;
	private final EventRepository eventRepository;

	public Slice<PartyResponseDto> getPartyList(Long eventId, Pageable pageable) {
		return partyRepository.findPartyListByEventId(eventId, pageable);
	}

	@Transactional
	public void createParty(Long eventId, Long destinationId, User user, String title, int maximum, int duration) {
		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new BaseException(ErrorCode.EVENT_NOT_FOUND));
		Event destination = eventRepository.findById(destinationId)
			.orElseThrow(() -> new BaseException(ErrorCode.EVENT_NOT_FOUND));

		Party party = Party.builder()
			.event(event)
			.destination(destination)
			.user(user)
			.title(title)
			.maximum(maximum)
			.duration(duration)
			.expiredAt(LocalDateTime.now().plusMinutes(duration))
			.build();

		partyRepository.save(party);
	}

	@Transactional
	public void updateParty(Long destinationId, Long partyId, User user, String title, int maximum, int duration) {
		Event destination = eventRepository.findById(destinationId)
			.orElseThrow(() -> new BaseException(ErrorCode.EVENT_NOT_FOUND));

		Party party = partyRepository.findById(partyId)
			.orElseThrow(() -> new BaseException(ErrorCode.PARTY_NOT_FOUND));

		party.updateParty(destination, title, maximum, duration);
	}

	@Transactional
	public void deleteParty(Long partyId) {
		Party party = partyRepository.findById(partyId)
			.orElseThrow(() -> new BaseException(ErrorCode.PARTY_NOT_FOUND));

		partyRepository.delete(party);
	}
}
