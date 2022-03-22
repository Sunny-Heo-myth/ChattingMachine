package com.sunny.chattingmachine.exception;

public abstract class BaseException extends RuntimeException{
    public abstract BaseExceptionType getExceptionType();
}
