package com.idle.togeduck.domain.user.serivce;

import static com.idle.togeduck.global.response.ErrorCode.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idle.togeduck.domain.celebrity.repository.CelebrityRepository;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.event.repository.EventRepository;
import com.idle.togeduck.domain.user.dto.HistoryEventResponseDto;
import com.idle.togeduck.domain.user.dto.HistoryResponseDto;
import com.idle.togeduck.domain.user.dto.RouteResponseDto;
import com.idle.togeduck.domain.user.entity.History;
import com.idle.togeduck.domain.user.entity.HistoryEvent;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.HistoryEventRepository;
import com.idle.togeduck.domain.user.repository.HistoryRepository;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {
	private final HistoryRepository historyRepository;
	private final HistoryEventRepository historyEventRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final CelebrityRepository celebrityRepository;

	@Transactional
	public Long createHistory(Long celebrityId, Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		History history = historyRepository.save(History.builder()
			.user(user)
			.date(LocalDate.now())
			.name(LocalDate.now().toString())
			.celebrityId(celebrityId)
			.build());
		return history.getId();
	}

	@Transactional
	public void addVisitedEvent(Long historyId, Long eventId) {
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new BaseException(EVENT_NOT_FOUND));
		History history = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(HISTORY_NOT_FOUND));

		historyEventRepository.save(HistoryEvent.builder()
			.event(event)
			.history(history)
			.build());
	}

	public List<HistoryResponseDto> joinHistory(Long celebrityId, Long userId) {
		celebrityRepository.findById(celebrityId)
			.orElseThrow(() -> new BaseException(CELEBRITY_NOT_FOUND));
		userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		return historyRepository.findMyHistory(celebrityId, userId);
	}

	public RouteResponseDto getRoutes(Long historyId, Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
		History history = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(HISTORY_NOT_FOUND));

		List<HistoryEventResponseDto> events = historyEventRepository.findEvents(historyId);

		return new RouteResponseDto(history.getRoute(), events);
	}

	@Transactional
	public void updateName(Long historyId, String historyName, Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
		History history = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(HISTORY_NOT_FOUND));

		history.updateName(historyName);
	}

	@Transactional
	public void updateRoute(Long historyId, String route, Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
		History history = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(HISTORY_NOT_FOUND));

		history.updateRoute(route);
	}

	@Transactional
	public void deleteHistory(Long historyId, Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
		History history = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(HISTORY_NOT_FOUND));

		historyRepository.delete(history);
	}

}
