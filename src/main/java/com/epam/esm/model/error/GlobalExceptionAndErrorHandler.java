package com.epam.esm.model.error;

import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAndErrorHandler {
    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionAndErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> customHandleNotFound(Exception e) {
        CustomError customError = new CustomError();
        customError.setErrorMessage(messageSource.getMessage(e.getMessage(), ((NotFoundException) e).getArgs(),
                LocaleContextHolder.getLocale()));
        customError.setErrorCode(Integer.toString(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> customHandleBadRequest(Exception e) {
        CustomError customError = new CustomError();
        customError.setErrorMessage(messageSource.getMessage(e.getMessage(), ((BadRequestException) e).getArgs(),
                LocaleContextHolder.getLocale()));
        customError.setErrorCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }
}
