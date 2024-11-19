package com.teamjhw.sse.global.security.handler;

import com.teamjhw.sse.global.exception.ErrorCode;
import com.teamjhw.sse.global.exception.exceptionType.CustomJWTException;
import com.teamjhw.sse.global.utils.SecurityErrorResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CustomJWTException e = new CustomJWTException(ErrorCode.ACCESS_DENIED_ERROR);
        SecurityErrorResponseUtil.setSecurityErrorResponse(e, response, request);
    }
}
