package com.teamjhw.bestfriend.entity;

import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.MatchException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchResult {
    NOT_FINISHED(0),
    HOME(1),
    DRAW(2),
    AWAY(3),
    CANCEL(4),
    STOP(5);

    private final int key;

    /*
     * 정수 -> Enum 변환 메서드
     */
    public static MatchResult fromKey(int key) {
        if (key > 5 || key < 0) {
            throw new MatchException(ErrorCode.UNDEFINED_MATCH_RESULT);
        }
        return MatchResult.values()[key];
    }
}
