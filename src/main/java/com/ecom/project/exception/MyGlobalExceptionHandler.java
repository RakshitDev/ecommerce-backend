package com.ecom.project.exception;

import com.ecom.project.config.ErrorStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class MyGlobalExceptionHandler {


    @ExceptionHandler(ApiException.class)
    ResponseEntity<ErrorStructure<String>> handleApiException(ApiException e) {
        ErrorStructure<String> error = new ErrorStructure<>();
        error.setMessage(e.getMessage());
        error.setCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorStructure<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        StringBuilder errorMessage = new StringBuilder("Validation Errors: ");

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMessage.append("[")
                    .append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("] ");
        });

        ErrorStructure<String> error = new ErrorStructure<>();
        error.setMessage(errorMessage.toString().trim());
        error.setCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
