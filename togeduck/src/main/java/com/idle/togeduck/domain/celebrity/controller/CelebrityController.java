package com.idle.togeduck.domain.celebrity.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.idle.togeduck.domain.celebrity.entity.Celebrity;
import com.idle.togeduck.domain.celebrity.service.CelebrityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/celebrities")
@RequiredArgsConstructor
public class CelebrityController {

	private final CelebrityService celebrityService;

	@GetMapping
	public List<Celebrity> getAllCelebrity(@RequestParam String name, String nickname) throws
		IOException { //  getAllCelebrity(CelebrityRequestDto celebrityRequestDto) throws IOException

		// System.out.println(name + "," + "nickname");

		return celebrityService.getAllCelebrity(name, nickname);
	}
}
