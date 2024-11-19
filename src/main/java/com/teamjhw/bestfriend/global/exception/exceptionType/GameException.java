package com.teamjhw.bestfriend.global.exception.exceptionType;

import com.teamjhw.bestfriend.global.exception.CustomException;
import com.teamjhw.bestfriend.global.exception.ErrorCode;

public class GameException extends CustomException {

    public GameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
