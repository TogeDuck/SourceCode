package com.idle.togeduck.domain.user.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "googleUser", url = "https://www.googleapis.com")
public interface GoogleUserApi {
	@GetMapping("/oauth2/v2/userinfo")
	ResponseEntity<String> getUserInfo(@RequestParam("access_token") String accessToken);
}