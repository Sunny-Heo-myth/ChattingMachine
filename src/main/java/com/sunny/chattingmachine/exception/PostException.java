package com.sunny.chattingmachine.exception;

public class PostException extends BaseException {

    private final BaseExceptionType baseExceptionType;

    public PostException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
