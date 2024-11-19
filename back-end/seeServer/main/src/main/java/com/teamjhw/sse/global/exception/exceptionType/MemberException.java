package com.teamjhw.sse.global.exception.exceptionType;

import com.teamjhw.sse.global.exception.CustomException;
import com.teamjhw.sse.global.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) { super(errorCode); }
}
