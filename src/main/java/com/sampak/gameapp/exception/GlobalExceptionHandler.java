package com.sampak.gameapp.exception;

import com.sampak.gameapp.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Domyślne wartości dla nieoczekiwanych wyjątków
        String errorCode = "UNEXPECTED_ERROR";
        String message = "An unexpected error occurred";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Obsługa wyjątków typu AppException
        if (ex instanceof AppException) {
            AppException appException = (AppException) ex;
            errorCode = appException.getErrorCode();
            message = appException.getMessage();
            status = appException.getStatus();
        }

        // Tworzenie odpowiedzi
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message, status.value());
        return new ResponseEntity<>(errorResponse, status);
    }
}
