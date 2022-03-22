package com.sunny.chattingmachine.exception;

import org.springframework.http.HttpStatus;

public enum FileExceptionType implements BaseExceptionType{
    FILE_NOT_FOUND(10000, HttpStatus.NOT_FOUND, "file not found"),
    FILE_CANNOT_SAVE(10001, HttpStatus.BAD_REQUEST, "failed to save file."),
    FILE_CANNOT_DELETE(10002, HttpStatus.BAD_REQUEST, "failed to delete file.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    FileExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
