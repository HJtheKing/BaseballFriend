package com.teamjhw.sse.global.exception.exceptionType;

import com.teamjhw.sse.global.exception.CustomException;
import com.teamjhw.sse.global.exception.ErrorCode;

public class CustomJWTException extends CustomException {

    public CustomJWTException(ErrorCode errorCode) {
        super(errorCode);
    }
}
