package com.sampak.gameapp.exception;

import com.sampak.gameapp.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });

        ObjectError exception = ex.getBindingResult().getAllErrors().get(0);

        String errorMessage = exception.getDefaultMessage();

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, errorMessage, 400    );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

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
