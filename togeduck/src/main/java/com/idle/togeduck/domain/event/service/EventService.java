package com.idle.togeduck.domain.event.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.event.dto.AllEventResponseDto;
import com.idle.togeduck.domain.event.dto.JoinEventResponseDto;
import com.idle.togeduck.domain.event.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {
	private final EventRepository eventRepository;

	public AllEventResponseDto getEvents(Long celebrityId, LocalDate startDate, LocalDate endDate,
		Long userId) {
		List<JoinEventResponseDto> allEvents = eventRepository.findEventList(celebrityId, startDate, endDate, userId);
		List<JoinEventResponseDto> past = new ArrayList<>();
		List<JoinEventResponseDto> today = new ArrayList<>();
		List<JoinEventResponseDto> later = new ArrayList<>();

		// LocalDate cur = LocalDate.of(2023, 12, 5);
		LocalDate cur = LocalDate.now();

		for (JoinEventResponseDto dto : allEvents) {
			if (dto.endDate().isBefore(cur)) {
				continue;
			}
			(dto.startDate().isBefore(cur) ? today : later).add(dto);
		}

		return new AllEventResponseDto(past, today, later);
	}
}
