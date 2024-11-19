package com.teamjhw.bestfriend.global.exception.exceptionType;

import com.teamjhw.bestfriend.global.exception.CustomException;
import com.teamjhw.bestfriend.global.exception.ErrorCode;

public class MatchException extends CustomException {

    public MatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
