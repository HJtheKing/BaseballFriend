package com.teamjhw.sse.global.exception;

import com.teamjhw.sse.global.exception.exceptionType.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ! 기본 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 오류 : [UnexpectedException] : ", e);
        return ErrorResponse.toResponseEntity(ErrorCode.UNEXPECTED_ERROR);
    }

    // ! 회원 관련 오류
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        log.error("[MemberException] : {} is occurred", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // ! SSE가 연결 시간 초과 오류
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e) {
        log.warn("[AsyncRequestTimeoutException] : {} is occurred", e.getMessage());
        return ResponseEntity.noContent().build();
    }

    // ! SSE가 연결된 상태에서 클라이언트가 접속을 종료할 발생하는 오류
    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public ResponseEntity<ErrorResponse> handleAsyncRequestNotUsableException(AsyncRequestNotUsableException e) {
        log.warn("[AsyncRequestNotUsableException] : {} is occurred", e.getMessage());
        return ResponseEntity.noContent().build();
    }

}
