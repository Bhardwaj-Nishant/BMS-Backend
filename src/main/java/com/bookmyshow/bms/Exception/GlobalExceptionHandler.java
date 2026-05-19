package com.bookmyshow.bms.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(resourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(resourceNotFoundException ex, WebRequest request) {

        errorResponse errorDetails = new errorResponse(
                new Date(),
                "Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(seatUnavailableException.class)
    public ResponseEntity<?> seatUnavailableException(seatUnavailableException ex, WebRequest request) {

        errorResponse errorDetails = new errorResponse(
                new Date(),
                "Bad Request",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {

        errorResponse errorDetails = new errorResponse(
                new Date(),
                "Server Error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}