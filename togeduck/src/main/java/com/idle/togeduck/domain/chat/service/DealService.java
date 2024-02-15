package com.idle.togeduck.domain.chat.service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.idle.togeduck.domain.chat.dto.DealRequestDto;
import com.idle.togeduck.domain.chat.dto.TradeDto;
import com.idle.togeduck.domain.chat.entity.Chat;
import com.idle.togeduck.domain.chat.entity.Deal;
import com.idle.togeduck.domain.chat.entity.DealStatus;
import com.idle.togeduck.domain.chat.entity.Trade;
import com.idle.togeduck.domain.chat.entity.UserChat;
import com.idle.togeduck.domain.chat.repository.ChatRepository;
import com.idle.togeduck.domain.chat.repository.DealRepository;
import com.idle.togeduck.domain.chat.repository.MessageRepository;
import com.idle.togeduck.domain.chat.repository.TradeRepository;
import com.idle.togeduck.domain.chat.repository.UserChatRepository;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DealService {

	private final DealRepository dealRepository;
	private final UserRepository userRepository;
	private final TradeRepository tradeRepository;
	private final ChatRepository chatRepository;
	private final MessageRepository messageRepository;
	private final FirebaseMessaging firebaseMessaging;
	private final UserChatRepository userChatRepository;

	//거래 요청
	public void createDeal(Long userId, Long targetId, Long myTradeId) {
		Trade target = tradeRepository.findByIdWithUser(targetId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));
		Trade myTrade = tradeRepository.findById(myTradeId)
			.orElseThrow(() -> new BaseException(ErrorCode.TRADE_NOT_FOUND));

		Deal deal = Deal.builder()
			.target(target)
			.myTrade(myTrade)
			.status(DealStatus.PENDING)
			.build();

		dealRepository.save(deal);
		log.info("dealId: {}", deal.getId());
		log.info("target.getUser().getDeviceToken() : {}", target.getUser().getDeviceToken());
		sendDealNotification(target.getUser().getDeviceToken(), deal.getId(), "거래 요청 도착", "request");
	}

	//거래 수락
	public void acceptDeal(Long dealId) {
		// 거래 수락 상태 변경
		Deal deal = dealRepository.findByIdWithTarget(dealId)
			.orElseThrow(() -> new BaseException(ErrorCode.DEAL_NOT_FOUND));
		deal.setStatus(DealStatus.ACCEPTED);

		dealRepository.delete(deal);
		tradeRepository.delete(deal.getMyTrade());
		tradeRepository.delete(deal.getTarget());

		//채팅방 생성
		Chat chat = Chat.builder()
			.quest(deal.getTarget())
			.maximum(2)
			.expiredAt(LocalDateTime.now().plusDays(1L))
			.build();
		chatRepository.save(chat);

		//채팅방 참여
		UserChat targetUserChat = UserChat.builder()
			.chat(chat)
			.user(deal.getTarget().getUser())
			.build();
		userChatRepository.save(targetUserChat);

		UserChat myUserChat = UserChat.builder()
			.chat(chat)
			.user(deal.getMyTrade().getUser())
			.build();
		userChatRepository.save(myUserChat);

		//거래 수락 성공 요청 전송
		sendDealNotificationWithChatId(deal.getTarget().getUser().getDeviceToken(), deal.getId(), "거래 요청 수락됨",
			"accept", chat.getId());
		sendDealNotificationWithChatId(deal.getMyTrade().getUser().getDeviceToken(), deal.getId(), "거래 요청 수락됨",
			"accept", chat.getId());
	}

	//거래 거절
	public void rejectDeal(Long dealId) {
		Deal deal = dealRepository.findByIdWithTarget(dealId)
			.orElseThrow(() -> new BaseException(ErrorCode.DEAL_NOT_FOUND));
		deal.setStatus(DealStatus.REJECTED);
		//거래 수락 성공 요청 전송
		sendDealNotification(deal.getMyTrade().getUser().getDeviceToken(), deal.getId(), "거래 요청 거절됨", "reject");
	}

	private void sendDealNotification(String deviceToken, Long dealId, String body, String type) {
		Message fcmMessage = Message.builder()
			.setToken(deviceToken)
			.setNotification(
				Notification.builder()
					.setTitle("Togeduck")
					.setBody(body)
					.build()
			)
			.putData("dealId", dealId.toString())
			.putData("type", type)
			.build();

		try {
			String response = firebaseMessaging.sendAsync(fcmMessage).get();
		} catch (InterruptedException e) {
			throw new BaseException(ErrorCode.FIREBASE_INTERRUPTED);
		} catch (ExecutionException e) {
			throw new BaseException(ErrorCode.FIREBASE_EXECUTION);
		}
	}

	private void sendDealNotificationWithChatId(String deviceToken, Long dealId, String body, String type,
		Long chatId) {
		Message fcmMessage = Message.builder()
			.setToken(deviceToken)
			.setNotification(
				Notification.builder()
					.setTitle("Togeduck")
					.setBody(body)
					.build()
			)
			.putData("dealId", dealId.toString())
			.putData("type", type)
			.putData("chatId", chatId.toString())
			.build();

		try {
			String response = firebaseMessaging.sendAsync(fcmMessage).get();
		} catch (InterruptedException e) {
			throw new BaseException(ErrorCode.FIREBASE_INTERRUPTED);
		} catch (ExecutionException e) {
			throw new BaseException(ErrorCode.FIREBASE_EXECUTION);
		}
	}

	public DealRequestDto getDeal(Long dealId) {
		Deal deal = dealRepository.findByIdWithTarget(dealId)
			.orElseThrow(() -> new BaseException(ErrorCode.DEAL_NOT_FOUND));
		Long targetId = deal.getTarget().getId();
		Long myTradeId = deal.getMyTrade().getId();
		Trade trade = tradeRepository.findById(targetId).get();
		Trade myTrade = tradeRepository.findById(myTradeId).get();
		return new DealRequestDto(
			new TradeDto(trade.getId(), trade.getContent(), trade.getImage(), trade.getDuration(), trade.getCreatedAt(),
				trade.getExpiredAt(), true),
			new TradeDto(myTrade.getId(), myTrade.getContent(), myTrade.getImage(), myTrade.getDuration(),
				myTrade.getCreatedAt(), myTrade.getExpiredAt(), false));
	}
}
