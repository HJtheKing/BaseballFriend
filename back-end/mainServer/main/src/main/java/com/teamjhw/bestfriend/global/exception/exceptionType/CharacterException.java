package com.teamjhw.bestfriend.global.exception.exceptionType;

import com.teamjhw.bestfriend.global.exception.CustomException;
import com.teamjhw.bestfriend.global.exception.ErrorCode;

public class CharacterException extends CustomException {

    public CharacterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
