package com.teamjhw.bestfriend.global.security.filter;

import com.teamjhw.bestfriend.global.exception.exceptionType.CustomJWTException;
import com.teamjhw.bestfriend.global.security.util.SecurityErrorResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTCheckExceptionFilter extends OncePerRequestFilter {

    /*
     * JWT CheckFilter를 실행하고 JWT CheckFilter에서 발생한 예외 사항을 처리함
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomJWTException e) {
            SecurityErrorResponseUtil.setSecurityErrorResponse(e, response, request);
        }
    }
}
