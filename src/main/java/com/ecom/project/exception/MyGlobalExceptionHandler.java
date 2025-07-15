package com.ecom.project.exception;

import com.ecom.project.config.ErrorStructure;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("timestamp", LocalDateTime.now());
            errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
            errorDetails.put("error", "Validation Failed");

            // Extract messages from constraint violations
            List<String> errors = ex.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .toList();

            errorDetails.put("message", errors);
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
    }


