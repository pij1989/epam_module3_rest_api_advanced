package com.epam.esm.model.exception;

public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        this(message, new Object[]{});
    }

    public NotFoundException(String message, Object[] args) {
        super(message, args);
    }
}
