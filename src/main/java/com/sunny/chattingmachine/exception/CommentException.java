package com.sunny.chattingmachine.exception;

public class CommentException extends BaseException{

    private final BaseExceptionType baseExceptionType;

    public CommentException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
