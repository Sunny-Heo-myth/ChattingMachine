package com.sunny.chattingmachine.exception;

public class OverlappedIdException extends RuntimeException {
    private String accountId;

    public OverlappedIdException(String accountId) {
        this.accountId = accountId;
    }

    public OverlappedIdException(String accountId, Throwable throwable) {
        super(throwable);
        this.accountId = accountId;
    }

}
