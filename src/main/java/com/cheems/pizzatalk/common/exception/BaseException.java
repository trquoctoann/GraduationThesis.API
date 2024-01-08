package com.cheems.pizzatalk.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private final String message;

    private final HttpStatus httpStatus;

    public BaseException(String message) {
        this(message, null, null);
    }

    public BaseException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public BaseException(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
    }

    public BaseException(String message, HttpStatus httpStatus, Throwable cause) {
        super(cause);
        if (message == null) {
            throw new IllegalArgumentException("Message must not be null");
        }
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
