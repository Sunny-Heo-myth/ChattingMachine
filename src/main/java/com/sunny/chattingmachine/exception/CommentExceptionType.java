package com.sunny.chattingmachine.exception;

import org.springframework.http.HttpStatus;

public enum CommentExceptionType implements BaseExceptionType{
    COMMENT_NOT_FOUND(800, HttpStatus.NOT_FOUND, "Comment not found."),
    COMMENT_NOT_AUTHORITY_UPDATE(801, HttpStatus.FORBIDDEN, "Update comment forbidden."),
    COMMENT_NOT_AUTHORITY_DELETE(802, HttpStatus.FORBIDDEN, "Delete comment forbidden.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    CommentExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
