package com.teamjhw.bestfriend.global.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ! 예기치못한 에러 발생 시 반환 에러
    UNEXPECTED_ERROR("예기치 못한 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! 회원 관련
    MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_DEFAULT_ERROR("회원 관련 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    KAKAO_PARSING_ERROR("카카오 서버에서 정보를 받아올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_SETTINGS_FAILURE("설정변경에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! 경기 관련
    MATCH_NOT_FOUND("경기를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNDEFINED_MATCH_RESULT("올바르지 않은 경기 결과 값입니다.", HttpStatus.BAD_REQUEST),
    TEAM_NOT_FOUND("팀을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TEAM_RANK_ERROR("팀 순위를 조회할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MATCH_SCHEDULE_CRAWLING_ERROR("경기 일정 크롤링에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_MATCH_SCHEDULE("중복된 경기 일정입니다.", HttpStatus.CONFLICT),
    DUPLICATE_MATCH_RESULT("경기 결과가 이미 있습니다.", HttpStatus.CONFLICT),
    
    // ! 인증 인가 관련
    INVALID_TOKEN("유효한 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED_ERROR("접근 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    MALFORMED_TOKEN("잘못된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_TOKEN("지원하지 않는 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_DEFAULT_ERROR("토큰 처리 중 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_ENCODING("지원되지 않는 인코딩입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MAIL_SEND_ERROR("인증 메일 전송 도중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! 뉴스 관련
    NAVER_NEWS_DEFAULT_ERROR("네이버 API 응답을 읽는 데 실패했습니다.", HttpStatus.BAD_REQUEST),
    NAVER_PARSING_ERROR("네이버 서버에서 정보를 받아올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NAVER_CONNECT_FAILED("URL 연결에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    OPENAI_PARSING_ERROR("OPENAI 서버에서 정보를 받아올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MALFORMED_URL("잘못된 API URL입니다.", HttpStatus.BAD_REQUEST),
    NEWS_NOT_FOUND("뉴스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    S3_PARSING_ERROR("Image URL -> MultipartFile 변환에 실패했습니다..", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! JSON 파싱 관련
    JSON_PARSING_ERROR("JSON에서 정보를 받아올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // ! 게임 관련
    DUPLICATE_MATCH_PREDICTION("이미 참여한 경기 예측입니다.", HttpStatus.CONFLICT),
    INSUFFICIENT_GAME_MONEY("게임 머니가 부족합니다.", HttpStatus.BAD_REQUEST),
    PREDICTION_NOT_FOUND("경기 예측에 참여한 적이 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN_MATCH_PREDICTION("접근 가능한 경기가 아닙니다.", HttpStatus.FORBIDDEN),

    // ! 캐릭터 관련
    MONEY_AMOUNT_ERROR("돈의 액수가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    CHARACTER_NOT_FOUND("캐릭터가 존재 하지 않습니다.", HttpStatus.NOT_FOUND),
    CHARACTER_OWNERSHIP_ERROR("보유한 캐릭터입니다.", HttpStatus.BAD_REQUEST),
    CHARACTER_ITEM_NOT_OWNED("보유하지 않은 캐릭터의 아이템 구매는 불가입니다.", HttpStatus.NOT_FOUND),
    CHARACTER_NOT_OWNERSHIP("보유하지 않은 캐릭입니다.", HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND("아이템이 존재 하지 않습니다.", HttpStatus.NOT_FOUND),
    ITEM_OWNERSHIP_ERROR("보유한 아이템입니다.", HttpStatus.BAD_REQUEST),
    ITEM_NOT_OWNERSHIP("보유하지 않은 아이템입니다.", HttpStatus.NOT_FOUND),
    BACKGROUND_NOT_FOUND("배경이 존재 하지 않습니다.", HttpStatus.NOT_FOUND),
    BACKGROUND_OWNERSHIP_ERROR("보유한 배경입니다.", HttpStatus.BAD_REQUEST),
    BACKGROUND_NOT_OWNERSHIP("보유하지 않은 배경입니다.", HttpStatus.NOT_FOUND),
    UNDEFINED_ITEM_CATEGORY("아이템 카테고리에 없는 값입니다.", HttpStatus.BAD_REQUEST),
    INVALID_HEAD_CATEGORY("카테고리가 머리가 아닙니다.", HttpStatus.BAD_REQUEST),
    INVALID_BODY_CATEGORY("카테고리가 몸통이 아닙니다.", HttpStatus.BAD_REQUEST),
    INVALID_ARM_CATEGORY("카테고리가 팔이 아닙니다.", HttpStatus.BAD_REQUEST),

    // ! 회원가입 관련
    NOT_SAME_PW("비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL("이메일로 가입된 계정이 존재합니다.", HttpStatus.CONFLICT),
    MAIL_AUTH_CODE_NOT_FOUND("인증 코드가 존재하지 않습니다.", HttpStatus.NOT_FOUND)

    ;


    private final String message;
    private final HttpStatus httpStatus;
}
