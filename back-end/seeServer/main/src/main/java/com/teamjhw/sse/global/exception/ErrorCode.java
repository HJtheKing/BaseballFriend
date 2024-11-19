package com.teamjhw.sse.global.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ! 예기치못한 에러 발생 시 반환 에러
    UNEXPECTED_ERROR("예기치 못한 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! 인증 인가 관련
    INVALID_TOKEN("유효한 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED_ERROR("접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    MALFORMED_TOKEN("잘못된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN("지원하지 않는 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_DEFAULT_ERROR("토큰 처리 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_ENCODING("지원되지 않는 인코딩입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! 멤버 관련
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    ;

    private final String message;
    private final HttpStatus httpStatus;
}
