package com.cheems.pizzatalk.common.exception;

import org.springframework.http.HttpStatus;

public class AdapterException extends BaseException {

    public AdapterException(String message) {
        super(message);
    }

    public AdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdapterException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public AdapterException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, httpStatus, cause);
    }
}
