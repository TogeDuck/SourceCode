package com.idle.togeduck.domain.event.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.event.dto.ShareRespondDto;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.event.entity.Share;
import com.idle.togeduck.domain.event.repository.EventRepository;
import com.idle.togeduck.domain.event.repository.ShareRepository;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;
import com.idle.togeduck.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShareService {

	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final ShareRepository shareRepository;
	private final S3Service s3Service;

	public Slice<ShareRespondDto> getShareList(Long userId, Long eventId, Pageable pageable) {
		return shareRepository.findSliceByEventId(userId, eventId, pageable);
	}

	@Transactional
	public void createShare(
		Long userId,
		Long eventId,
		MultipartFile image,
		String title,
		String content,
		Integer duration
	) {
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new BaseException(ErrorCode.EVENT_NOT_FOUND));
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
		String imageUrl = s3Service.saveFile(image);
		shareRepository.save(Share.builder()
			.event(event)
			.user(user)
			.title(title)
			.content(content)
			.image(imageUrl)
			.duration(duration)
			.build());
	}

	@Transactional
	public void updateShare(
		Long shareId,
		MultipartFile image,
		String title,
		String content,
		Integer duration) {
		Share share = shareRepository.findById(shareId).orElseThrow(() -> new BaseException(ErrorCode.SHARE_NOT_FOUND));
		String imageUrl = s3Service.saveFile(image);
		share.updateShare(imageUrl, title, content, duration);

	}

	@Transactional
	public void deleteShare(Long shareId) {
		Share share = shareRepository.findById(shareId).orElseThrow(() -> new BaseException(ErrorCode.SHARE_NOT_FOUND));

		shareRepository.delete(share);

	}

}
