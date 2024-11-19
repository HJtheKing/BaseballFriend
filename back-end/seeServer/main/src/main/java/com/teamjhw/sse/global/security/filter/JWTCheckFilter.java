package com.teamjhw.sse.global.security.filter;

import com.teamjhw.sse.domain.member.dto.MemberDetails;
import com.teamjhw.sse.entity.MemberRole;
import com.teamjhw.sse.global.exception.ErrorCode;
import com.teamjhw.sse.global.exception.exceptionType.CustomJWTException;
import com.teamjhw.sse.global.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/*
 * * 모든 요청의 jwt를 검증하는 필터
 * */
@Log4j2
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    /*
     * * 토큰 검증없이 접근 가능한 api를 설정함
     * */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/bf/auth/login",
                "/swagger-ui",
                "/api-docs",
                "/images",
        };
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
//        return true; // 테스트를 위해 모두 true로 열어둠(임시)
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("-------jwt check filter-------");
        String accessToken = jwtUtil.getTokenFromHeader(request);

        // AccessToken 유효성 검사
        if (!jwtUtil.checkTokenExpired(accessToken)) {
            log.info("[accessToken is valid]");
            setAuthentication(accessToken);
        } else {
            log.info("[accessToken is invalid]");
            throw new CustomJWTException(ErrorCode.EXPIRED_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * * Security Context Holder 안에 Authentication을 저장함
     */
    private void setAuthentication(String accessToken) {
        log.info("--------setAuthentication-----");
        Map<String, Object> claims = jwtUtil.validateToken(accessToken);
        log.info("claims : " + claims);

        Long id = ((Number) claims.get("id")).longValue();
        MemberRole role = MemberRole.USER;

        MemberDetails memberDetails = new MemberDetails(id, role);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberDetails, "", memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
