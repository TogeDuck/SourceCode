package com.idle.togeduck.domain.user.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.idle.togeduck.domain.user.dto.TokenDto;
import com.idle.togeduck.global.YamlPropertySourceFactory;
import com.idle.togeduck.global.response.JwtCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class JwtProvider { // 유저 정보로 JWT 토큰을 만들거나 토큰을 바탕으로 유저 정보 가져옴

	private static final String AUTHORITIES_KEY = "auth";
	private static final String BEARER_TYPE = "Bearer";
	private Key key;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access-token-validity-in-milliseconds}")
	private Long accessTokenValidTime;

	@Value("${jwt.refresh-token-validity-in-milliseconds}")
	private Long refreshTokenValidTime;

	public JwtProvider() { // JWT 만들 때 사용하는 암호화 키값 생성
		byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secret 값을 base64로 디코딩
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(Authentication authentication, Long tokenValidTime) {
		// 권한 가져오기
		String authorities = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		Date now = new Date(); // 생성날짜
		Date expiration = new Date(now.getTime() + tokenValidTime); // 만료날짜

		Claims claims = Jwts.claims()
			.setSubject(authentication.getName()) // payload "sub" : "userId" (토큰제목)
			.setIssuedAt(now) // payload "iss" : 147621021 (토큰발급자)
			.setExpiration(expiration);  // payload "exp" : 151621022 (토큰만료시간)

		claims.put(AUTHORITIES_KEY, authorities); // payload "AUTHORITIES_KEY" : 'ROLE_USER' (권한)

		return Jwts.builder()
			.setClaims(claims)
			.signWith(key, SignatureAlgorithm.ES512) // header "alg" : "HS512"
			.compact();
	}

	// Access Token 생성
	public String createAccessToken(Authentication authentication) {
		return createToken(authentication, accessTokenValidTime);
	}

	// Refresh Token 생성
	public String createRefreshToken(Authentication authentication) {
		return createToken(authentication, refreshTokenValidTime);
	}

	public TokenDto createTokenDto(Authentication authentication) {

		return TokenDto.builder()
			.grantType(BEARER_TYPE)
			.accessToken(createAccessToken(authentication))
			.refreshToken(createRefreshToken(authentication))
			.accessTokenExpireDate(accessTokenValidTime)
			.build();
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJwt(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public Authentication getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);

		if (claims.get(AUTHORITIES_KEY) == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		// 클레임에 대한 정보
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// UserDetails 객체를 만들어서 Authentication 리턴
		UserDetails principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	public JwtCode validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return JwtCode.ACCESS;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
			return JwtCode.DENIED;
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토근입니다.");
			return JwtCode.EXPIRED;
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토근입니다.");
			return JwtCode.DENIED;
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
			return JwtCode.DENIED;
		}
	}
}
