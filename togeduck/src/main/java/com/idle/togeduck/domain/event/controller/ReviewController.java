package com.idle.togeduck.domain.event.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.event.service.ReviewService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping("/{eventId}")
	public ResponseEntity<BaseResponse<?>> getReviewList(@PathVariable Long eventId, Long userId, Pageable pageable) {
		return ResponseEntity.ok(
			new BaseResponse<>(HttpStatus.OK.value(), "리뷰를 불러왔습니다",
				reviewService.getReviewList(eventId, userId, pageable)));
	}

	@PostMapping("/{eventId}/reviews")
	public ResponseEntity<BaseResponse<?>> saveReview(@PathVariable Long eventId, Long userId,
		@RequestPart String content, @RequestPart MultipartFile image) {

		reviewService.saveReview(eventId, userId, content, image);

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(new BaseResponse<>(201, "리뷰 등록이 성공했습니다", null));
	}

	@DeleteMapping("/{eventId}/reviews/{reviewId}")
	public ResponseEntity<BaseResponse<?>> deleteReview(@PathVariable Long eventId, @PathVariable Long reviewId,
		Long userId) {

		reviewService.deleteReview(reviewId, userId);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "리뷰 삭제가 성공했습니다", null));
	}
}
