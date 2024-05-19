package ru.interview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.interview.exception.ProductNotFoundException;
import ru.interview.exception.ProductValidationException;
import ru.interview.response.ResponseBuilder;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductValidationException.class)
    public ResponseEntity<?> handleProductValidationException(ProductValidationException exception, HttpServletRequest request) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, exception, request.getRequestURI());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException exception, HttpServletRequest request) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, exception, request.getRequestURI());
    }
}
