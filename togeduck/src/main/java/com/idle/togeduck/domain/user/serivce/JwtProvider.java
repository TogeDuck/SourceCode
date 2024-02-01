// package com.idle.togeduck.domain.user.serivce;
//
// import java.security.Key;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Date;
// import java.util.stream.Collectors;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.PropertySource;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
//
// import com.idle.togeduck.global.YamlPropertySourceFactory;
// import com.idle.togeduck.global.response.JwtCode;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.UnsupportedJwtException;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
// public class JwtProvider {
//
// 	private static final String AUTHORITIES_KEY = "auth";
// 	private Key key;
//
// 	@Value("${jwt.secret}")
// 	private String secretKey;
//
// 	@Value("${jwt.expireTime}")
// 	private Long expireTime;
//
// 	@Value("${access-token-validity-in-seconds}")
// 	private Long accessTokenValidTime;
//
// 	@Value("${refresh-token-validity-in-seconds}")
// 	private Long refreshTokenValidTime;
//
// 	public void init() {
// 		byte[] keyBytes = Decoders.BASE64.decode(secretKey); // secret 값을 base64로 디코딩
// 		key = Keys.hmacShaKeyFor(keyBytes);
// 	}
//
// 	public String createToken(Authentication authentication, Long tokenValidTime) {
// 		String authorities = authentication.getAuthorities().stream()
// 			.map(GrantedAuthority::getAuthority)
// 			.collect(Collectors.joining(","));
//
// 		Date now = new Date();
// 		Date expiration = new Date(now.getTime() + tokenValidTime);
//
// 		return Jwts.builder()
// 			.setSubject(authentication.getName())
// 			.claim(AUTHORITIES_KEY, authorities)
// 			.setIssuedAt(now) // 발행 시간
// 			.setExpiration(expiration)
// 			.signWith(key, SignatureAlgorithm.ES512)
// 			.compact();
// 	}
//
// 	public String createAccessToken(Authentication authentication) {
// 		return createToken(authentication, accessTokenValidTime);
// 	}
//
// 	public String createRefreshToken(Authentication authentication) {
// 		return createToken(authentication, refreshTokenValidTime);
// 	}
//
// 	public Authentication getAuthentication(String token) {
// 		Claims claims = Jwts.parserBuilder()
// 			.setSigningKey(key)
// 			.build()
// 			.parseClaimsJwt(token)
// 			.getBody();
//
// 		Collection<? extends GrantedAuthority> authorities =
// 			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
// 				.map(SimpleGrantedAuthority::new)
// 				.collect(Collectors.toList());
//
// 		User principal = new User(claims.getSubject(), "", authorities);
// 		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
// 	}
//
// 	public JwtCode validateToken(String token) {
// 		try {
// 			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
// 			return JwtCode.ACCESS;
// 		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
// 			log.info("잘못된 JWT 서명입니다.");
// 			return JwtCode.DENIED;
// 		} catch (ExpiredJwtException e) {
// 			log.info("만료된 JWT 토근입니다.");
// 			return JwtCode.EXPIRED;
// 		} catch (UnsupportedJwtException e) {
// 			log.info("지원되지 않는 JWT 토근입니다.");
// 			return JwtCode.DENIED;
// 		} catch (IllegalArgumentException e) {
// 			log.info("JWT 토큰이 잘못되었습니다.");
// 			return JwtCode.DENIED;
// 		}
// 	}
// }
