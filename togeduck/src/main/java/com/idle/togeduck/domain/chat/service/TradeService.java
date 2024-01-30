package com.idle.togeduck.domain.chat.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.chat.dto.TradeResponseDto;
import com.idle.togeduck.domain.chat.entity.Trade;
import com.idle.togeduck.domain.chat.repository.TradeRepository;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.event.repository.EventRepository;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeService {

	private final TradeRepository tradeRepository;
	private final EventRepository eventRepository;

	public Slice<TradeResponseDto> getTradeList(Long eventId, Pageable pageable) {
		return tradeRepository.findSliceByEventId(eventId, pageable);
	}

	public TradeResponseDto getTrade(Long tradeId) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		return new TradeResponseDto(
			trade.getId(),
			trade.getContent(),
			trade.getImage(),
			trade.getDuration(),
			trade.getCreatedAt());
	}

	@Transactional
	public void createTrade(Long eventId, User user, MultipartFile image, String content, int duration) {
		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new BaseException(ErrorCode.EVENT_NOT_FOUND));

		Trade trade = Trade.builder()
			.event(event)
			.user(user)
			.image(image.getOriginalFilename())
			.content(content)
			.duration(duration)
			.build();

		tradeRepository.save(trade);
	}

	@Transactional
	public void updateTrade(Long tradeId, User user, MultipartFile image, String content, int duration) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		trade.updateTrade(image.getOriginalFilename(), content, duration);
	}

	@Transactional
	public void deleteTrade(Long tradeId, User user) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		tradeRepository.delete(trade);
	}

}
