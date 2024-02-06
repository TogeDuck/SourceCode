package com.idle.togeduck.domain.event.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idle.togeduck.domain.event.dto.ShareRequestDto;
import com.idle.togeduck.domain.event.dto.ShareRespondDto;
import com.idle.togeduck.domain.event.service.ShareService;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class ShareController {

	private final ShareService shareService;

	@GetMapping("/{eventId}/shares")
	public ResponseEntity<BaseResponse<Slice<ShareRespondDto>>> getShares(
		@PathVariable Long eventId,
		Pageable pageable) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", shareService.getShareList(1L, eventId, pageable)));
	}

	@PostMapping("/{eventId}/shares")
	public ResponseEntity<BaseResponse<?>> createShare(
		@PathVariable Long eventId,
		@RequestPart MultipartFile image,
		@RequestPart ShareRequestDto shareRequestDto) {
		Long userId = 1L;
		shareService.createShare(
			eventId,
			userId,
			image,
			shareRequestDto.title(),
			shareRequestDto.content(),
			shareRequestDto.duration());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", null));
	}

	@PatchMapping("/{eventId}/shares/{shareId}")
	public ResponseEntity<BaseResponse<?>> updateShare(
		@PathVariable Long eventId,
		@PathVariable Long shareId,
		@RequestPart MultipartFile image,
		@RequestPart ShareRequestDto shareRequestDto) {
		shareService.updateShare(
			shareId,
			image,
			shareRequestDto.title(),
			shareRequestDto.content(),
			shareRequestDto.duration());
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", null));
	}

	@DeleteMapping("/{eventId}/shares/{shareId}")
	public ResponseEntity<BaseResponse<?>> deleteShare(
		@PathVariable Long eventId,
		@PathVariable Long shareId) {
		shareService.deleteShare(shareId);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new BaseResponse<>(200, "success", null));
	}

}
