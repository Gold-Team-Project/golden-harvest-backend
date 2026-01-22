package com.teamgold.goldenharvest.common.security.jwt;

import com.teamgold.goldenharvest.common.security.CustomUserDetailsService;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;
    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct  // 시크릿키 초기화
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    //  access token 생성
    public String createAccessToken(User user) {
        Date now = new Date();
        // 토큰 발급 시점
        Date expirationDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().getRoleStatusId()) //  권한
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

    }
    //  refresh token 생성
    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().getRoleStatusId())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
    /* 토큰 정보 바탕으로 시큐리티 인증 객체 생성
    *  DB 조회 없이 토큰 내부 데이터만 사용해서 속도 높임 (Stateless 방식) */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String email = claims.getSubject(); //  이메일 추출

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        //  최종적인 인증 객체 생성하여 반환
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalStateException e) {
            log.error("JWT 토큰이 비어있거나 잘못되었습니다.");
        }
        return false;   // 위 예외 중 하나라도 걸리면 유효하지 않는 토큰

    }

    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }

    //  토큰에서 데이터를 안전하게 꺼내오는 메서드
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었더라도 그 안의 Payload 꺼낼 수 있음
            return e.getClaims();
        }
    }
    public long getExpiration(String token) {
        try {
            // 토큰의 만료 날짜를 가져옴
            Date expiration = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            // 현재 시간과의 차이를 계산
            long now = new Date().getTime();
            return (expiration.getTime() - now);
        } catch (Exception e) {
            return 0;
        }
    }
}
