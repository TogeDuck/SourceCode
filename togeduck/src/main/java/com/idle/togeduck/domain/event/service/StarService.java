package com.idle.togeduck.domain.event.service;

import static com.idle.togeduck.global.response.ErrorCode.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.event.dto.AllEventResponseDto;
import com.idle.togeduck.domain.event.dto.EventResponseDto;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.event.repository.EventRepository;
import com.idle.togeduck.domain.event.repository.StarRepository;
import com.idle.togeduck.domain.user.entity.Star;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StarService {
	private final StarRepository starRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;

	@Transactional
	public AllEventResponseDto getEvents(Long userId) {

		List<EventResponseDto> allEvents = starRepository.findEventListByUserId(userId);
		List<EventResponseDto> past = new ArrayList<>();
		List<EventResponseDto> today = new ArrayList<>();
		List<EventResponseDto> later = new ArrayList<>();

		LocalDate cur = LocalDate.now();
		for (EventResponseDto dto : allEvents) {
			if (dto.endDate().isBefore(cur)) {
				past.add(dto);
				continue;
			}
			(dto.startDate().isBefore(cur) ? today : later).add(dto);
		}

		return new AllEventResponseDto(past, today, later);
	}

	@Transactional
	public void addStar(Long userId, Long eventId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new BaseException(EVENT_NOT_FOUND));

		starRepository.save(Star.builder()
			.user(user)
			.event(event)
			.build());
	}

	@Transactional
	public void deleteStar(Long likeId, Long userId) {
		Star star = starRepository.findById(likeId).orElseThrow(() -> new BaseException(STAR_NOT_FOUND));

		starRepository.delete(star);
	}
}
