package com.teamjhw.sse.global.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamjhw.sse.global.exception.ErrorCode;
import com.teamjhw.sse.global.exception.ErrorResponse;
import com.teamjhw.sse.global.exception.exceptionType.CustomJWTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityErrorResponseUtil {
    /*
     * Security Filter에서 발생한 예외 사항을 ErrorResponse 형태로 변환함
     * */
    public static void setSecurityErrorResponse(CustomJWTException e, HttpServletResponse response, HttpServletRequest request)
            throws IOException {
        ErrorCode errorCode = e.getErrorCode();
        int status = errorCode.getHttpStatus().value();

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .status(status)
                                                   .errorCode(errorCode)
                                                   .errorMessage(errorCode.getMessage())
                                                   .build();
        // JSON으로 변환
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        // 응답에 Error Response 저장
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
