package com.epam.esm.model.exception;

public class BadRequestException extends RestException {

    public BadRequestException(String message) {
        this(message, new Object[]{});
    }

    public BadRequestException(String message, Object[] args) {
        super(message, args);
    }

    public BadRequestException(String message, Throwable cause) {
        this(message, cause, new Object[]{});
    }

    public BadRequestException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }
}
