package com.sunny.chattingmachine.exception;

public class AccountException extends BaseException{
    private final BaseExceptionType exceptionType;

    public AccountException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
