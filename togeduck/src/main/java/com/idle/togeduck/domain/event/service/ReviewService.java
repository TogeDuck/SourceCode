package com.idle.togeduck.domain.event.service;

import static com.idle.togeduck.global.response.ErrorCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.event.dto.ReviewResponseDto;
import com.idle.togeduck.domain.event.entity.Event;
import com.idle.togeduck.domain.event.entity.Review;
import com.idle.togeduck.domain.event.repository.EventRepository;
import com.idle.togeduck.domain.event.repository.ReviewRepository;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.domain.user.repository.UserRepository;
import com.idle.togeduck.global.response.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;
	private final S3Service s3Service;

	public Slice<ReviewResponseDto> getReviewList(Long eventId, Long userId, Pageable pageable) {
		return reviewRepository.findSliceByEventId(eventId, userId, pageable);
	}

	@Transactional
	public void saveReview(Long eventId, Long userId, String content, MultipartFile image) {

		Event event = eventRepository.findById(eventId).orElseThrow(() -> new BaseException(EVENT_NOT_FOUND));
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		String imagePath = null;
		if (image != null) {
			imagePath = s3Service.saveFile(image);
		}

		reviewRepository.save(Review.builder()
			.content(content)
			.event(event)
			.user(user)
			.image(imagePath)
			.build());
	}

	@Transactional
	public void deleteReview(Long reviewId, Long userId) {

		Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
			.orElseThrow(() -> new BaseException(REVIEW_NOT_FOUND));

		s3Service.deleteFile(review.getImage());

		reviewRepository.delete(review);
	}

}
