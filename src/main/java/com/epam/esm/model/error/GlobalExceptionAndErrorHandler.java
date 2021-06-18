package com.epam.esm.model.error;

import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionAndErrorHandler {
    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionAndErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({NotFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<Object> customHandleNotFound(Exception e) {
        CustomError customError = new CustomError();
        if (e instanceof NotFoundException) {
            customError.setErrorMessage(messageSource.getMessage(e.getMessage(), ((NotFoundException) e).getArgs(),
                    LocaleContextHolder.getLocale()));
        } else {
            customError.setErrorMessage(messageSource.getMessage(MessageKeyError.NOT_FOUND, null,
                    LocaleContextHolder.getLocale()));
        }
        customError.setErrorCode(Integer.toString(HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<Object> customHandleBadRequest(Exception e) {
        CustomError customError = new CustomError();
        if (e instanceof BadRequestException) {
            customError.setErrorMessage(messageSource.getMessage(e.getMessage(), ((BadRequestException) e).getArgs(),
                    LocaleContextHolder.getLocale()));
        } else {
            customError.setErrorMessage(messageSource.getMessage(MessageKeyError.BAD_REQUEST, null,
                    LocaleContextHolder.getLocale()));
        }
        customError.setErrorCode(Integer.toString(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowed() {
        CustomError customError = new CustomError();
        customError.setErrorMessage(messageSource.getMessage(MessageKeyError.METHOD_NOT_ALLOWED, null,
                LocaleContextHolder.getLocale()));
        customError.setErrorCode(Integer.toString(HttpStatus.METHOD_NOT_ALLOWED.value()));
        return new ResponseEntity<>(customError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleInternalServerError() {
        CustomError customError = new CustomError();
        customError.setErrorMessage(messageSource.getMessage(MessageKeyError.INTERNAL_SERVER_ERROR, null,
                LocaleContextHolder.getLocale()));
        customError.setErrorCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return new ResponseEntity<>(customError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
