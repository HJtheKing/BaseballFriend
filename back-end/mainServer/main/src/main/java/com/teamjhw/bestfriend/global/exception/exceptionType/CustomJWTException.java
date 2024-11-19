package com.teamjhw.bestfriend.global.exception.exceptionType;

import com.teamjhw.bestfriend.global.exception.CustomException;
import com.teamjhw.bestfriend.global.exception.ErrorCode;

public class CustomJWTException extends CustomException {

    public CustomJWTException(ErrorCode errorCode) {
        super(errorCode);
    }
}
