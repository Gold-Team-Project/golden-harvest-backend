package com.teamgold.goldenharvest.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {
        //  요청 헤더에서 토큰 문자열 추출
        String token = resolveToken(request);

        //  토큰이 존재하고, 유효성 검사(만료 여부 + Redis 블랙리스트 체크)를 통과하는지 확인
        if(token != null && jwtTokenProvider.validateToken(token)) {

            // redis 블랙리스트 체크
            String isBlackList = (String) redisTemplate.opsForValue().get("BL:" + token);

            if (isBlackList != null) {
                log.warn("블랙리스트에 등록된 토큰으로 접근 시도: {}", token);
                // 블랙리스트라면 인증 객체를 저장하지 않고 바로 다음 필터로 넘깁니다.
                // 이후 SecurityConfig 설정에 따라 401 또는 403 에러가 발생하게 됩니다.
                filterChain.doFilter(request, response);
                return;
            }

            try {
            // 토큰이 유효 할경우 토큰 내 정보 기반으로 인증객체 생성(받아옴)
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 생성된 객체를 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("정상적인 토큰입니다. SecurityContext에 인증 정보를 저장했습니다. 유저: {}", authentication.getName());
        } catch(Exception ex) {
                // 인증 객체 생성 중 발생할 수 있는 예상치 못한 에러 처리
            log.error("SecurityContext에 인증 정보를 저장할 수 없습니다: {}", ex.getMessage());
            }
        } else {
            /* 토큰이 없거나 유효하지 않은 경우, 별도의 처리를 하지 않고 다음 필터로 넘김
            * 이후 SecurityConfig에서 권한이 필요한 API라면 바로 차단됨 */
            log.trace("유효한 JWT 토큰이 없거나 만료되었습니다. URL: {}", request.getRequestURI());
    }
        filterChain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // 헤더에서 Authorization 키 값을 가져옴
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim(); // bearer 이후 실제 토큰만 잘라서 반환
        }
        return null;
    }
}
