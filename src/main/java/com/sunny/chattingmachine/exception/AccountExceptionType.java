package com.sunny.chattingmachine.exception;

import org.springframework.http.HttpStatus;

public enum AccountExceptionType implements BaseExceptionType{

    ACCOUNT_NOT_FOUND(600, HttpStatus.NOT_FOUND, "Account not found."),
    WRONG_PASSWORD(601, HttpStatus.BAD_REQUEST, "Wrong password."),
    ACCOUNTID_ALREADY_EXIST(602, HttpStatus.CONFLICT, "Account id already exists.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    AccountExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
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
