package com.teamjhw.bestfriend.global.exception.exceptionType;

import com.teamjhw.bestfriend.global.exception.CustomException;
import com.teamjhw.bestfriend.global.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
