package com.teamjhw.bestfriend.global.exception;

import com.teamjhw.bestfriend.global.common.response.BodyValidationExceptionResponse;
import com.teamjhw.bestfriend.global.exception.exceptionType.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        log.debug("[MemberException] : {} is occurred", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // ! 경기 관련 오류
    @ExceptionHandler(MatchException.class)
    public ResponseEntity<ErrorResponse> handleMatchException(MatchException e) {
        log.debug("[MatchException] : {} is occurred", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // ! 뉴스 관련 오류
    @ExceptionHandler(NewsException.class)
    public ResponseEntity<ErrorResponse> handleNewsException(NewsException e) {
        log.debug("[NewsException] : {} is occurred", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // ! 게임 관련 오류
    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponse> handleGameException(GameException e) {
        log.debug("[GameException] : {} is occurred", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // ! 케릭터 관련 오류
    @ExceptionHandler(CharacterException.class)
    public ResponseEntity<ErrorResponse> handleCharacterException(CharacterException e) {
        log.debug("[CharacterException] : {} is occurred", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    // ! RequestBody DTO 속성에 대한 validation 검증 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<BodyValidationExceptionResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("field validation error : [MethodArgumentNotValidException] : ", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<BodyValidationExceptionResponse> errorResponse
                = fieldErrors.stream()
                             .map(error -> BodyValidationExceptionResponse.builder()
                                                                          .field(error.getField())
                                                                          .rejectedValue(error.getRejectedValue())
                                                                          .errorMessage(error.getDefaultMessage())
                                                                          .build())
                             .toList();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }
}
