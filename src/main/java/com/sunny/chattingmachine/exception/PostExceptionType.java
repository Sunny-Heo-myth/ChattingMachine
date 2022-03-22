package com.sunny.chattingmachine.exception;

import org.springframework.http.HttpStatus;

public enum PostExceptionType implements BaseExceptionType{
    POST_NOT_FOUND(700, HttpStatus.NOT_FOUND, "Post not found."),
    NOT_AUTHORITY_UPDATE_POST(701, HttpStatus.FORBIDDEN, "Update post forbidden."),
    NOT_AUTHORITY_DELETE_POST(702, HttpStatus.FORBIDDEN, "Delete post forbidden.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    PostExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
