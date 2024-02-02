package com.idle.togeduck.domain.chat.service;

import java.time.LocalDateTime;

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
import com.idle.togeduck.domain.event.service.S3Service;
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
	private final S3Service s3Service;

	public Slice<TradeResponseDto> getTradeList(Long userId, Long eventId, Pageable pageable) {
		return tradeRepository.findSliceByEventId(userId, eventId, pageable);
	}

	public Slice<TradeResponseDto> getMyTradeList(Long userId, Long eventId, Pageable pageable) {
		return tradeRepository.findSliceByEventIdAndUserId(userId, eventId, pageable);
	}

	public TradeResponseDto getTrade(Long userId, Long tradeId) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		return new TradeResponseDto(
			trade.getId(),
			trade.getContent(),
			trade.getImage(),
			trade.getDuration(),
			trade.getCreatedAt(),
			trade.getExpiredAt(),
			trade.getUser().getId() == userId);
	}

	@Transactional
	public void createTrade(Long eventId, User user, MultipartFile image, String content, int duration) {
		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new BaseException(ErrorCode.EVENT_NOT_FOUND));
		String imagePath = s3Service.saveFile(image);

		System.out.println(LocalDateTime.now());
		Trade trade = Trade.builder()
			.event(event)
			.user(user)
			.image(imagePath)
			.content(content)
			.duration(duration)
			.expiredAt(LocalDateTime.now().plusMinutes(duration))
			.build();

		tradeRepository.save(trade);
	}

	@Transactional
	public void updateTrade(Long tradeId, User user, MultipartFile image, String content, int duration) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		s3Service.deleteFile(trade.getImage());
		String imagePath = s3Service.saveFile(image);
		trade.updateTrade(imagePath, content, duration);
	}

	@Transactional
	public void deleteTrade(Long tradeId, User user) {
		Trade trade = tradeRepository.findById(tradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		s3Service.deleteFile(trade.getImage());
		tradeRepository.delete(trade);
	}
}
