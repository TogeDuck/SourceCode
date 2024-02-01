package com.idle.togeduck.domain.chat.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.chat.dto.PartyRequestDto;
import com.idle.togeduck.domain.chat.dto.PartyResponseDto;
import com.idle.togeduck.domain.chat.service.PartyService;
import com.idle.togeduck.domain.user.entity.User;
import com.idle.togeduck.global.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PartyController {

	private final PartyService partyService;

	@GetMapping("/{eventId}/parties")
	public ResponseEntity<BaseResponse<Slice<PartyResponseDto>>> getPartyList(
		@PathVariable Long eventId,
		Pageable pageable) {
		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공",
			partyService.getPartyList(eventId, pageable)));
	}

	@PostMapping("/{eventId}/parties")
	public ResponseEntity<BaseResponse<?>> createParty(
		@PathVariable Long eventId,
		@RequestBody PartyRequestDto partyRequestDto) {

		User user = User.builder().id(1L).build();

		partyService.createParty(eventId, partyRequestDto.destinationId(), user, partyRequestDto.title(),
			partyRequestDto.maximum(), partyRequestDto.duration());

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

	@PatchMapping("/{eventId}/parties/{partyId}")
	public ResponseEntity<BaseResponse<?>> updateParty(
		@PathVariable Long eventId,
		@PathVariable Long partyId,
		@RequestBody PartyRequestDto partyRequestDto) {

		User user = User.builder().id(1L).build();

		partyService.updateParty(eventId, partyId, user, partyRequestDto.title(),
			partyRequestDto.maximum(), partyRequestDto.duration());

		return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), "성공", null));
	}

}
