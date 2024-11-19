package com.teamjhw.bestfriend.global.exception.exceptionType;

import com.teamjhw.bestfriend.global.exception.CustomException;
import com.teamjhw.bestfriend.global.exception.ErrorCode;

public class NewsException extends CustomException {
    public NewsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
