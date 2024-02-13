package com.idle.togeduck.domain.event.service;

import static com.idle.togeduck.global.response.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.event.dto.AllEventResponseDto;
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
	private final EventService eventService;

	@Transactional
	public AllEventResponseDto getEvents(Long userId) {
		return eventService.classifyEvents(starRepository.findEventListByUserId(userId));
	}

	@Transactional
	public void addStar(Long userId, Long eventId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new BaseException(EVENT_NOT_FOUND));

		Optional<Star> star = starRepository.findByUserIdAndEventId(userId, eventId);
		if (star.isEmpty()) {
			starRepository.save(Star.builder()
				.user(user)
				.event(event)
				.build());
		} else {
			throw new BaseException(STAR_DUPLICATED);
		}

	}

	@Transactional
	public void deleteStar(Long eventId) {
		Star star = starRepository.findByEventId(eventId)
			.orElseThrow(() -> new BaseException(STAR_NOT_FOUND));

		starRepository.delete(star);
	}

}
