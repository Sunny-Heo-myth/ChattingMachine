package com.sunny.chattingmachine.exception;

public class FileException extends BaseException{
    private final BaseExceptionType exceptionType;

    public FileException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
