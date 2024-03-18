package com.example.letsplay.controller;

import com.example.letsplay.exceptions.*;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorsController {


    @ExceptionHandler(DataNotValidatedException.class)
    public ResponseEntity<String> handleDataNotValidatedException(DataNotValidatedException ex) {
        // Extract the custom Error object from the exception
        Error error = (Error) ex.getCause();
        // Build the response message
        String errorMessage = "Data not validated";
        if (error != null && error.getMessage() != null) {
            errorMessage += ": " + error.getMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }


    @ExceptionHandler(InstanceUndefinedException.class)
    public ResponseEntity<String> handleInstanceUndefinedException(InstanceUndefinedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(ExistingEmailException.class)
    public ResponseEntity<String> handleExistingEmailException(ExistingEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(ExistingProductException.class)
    public  ResponseEntity<String> handleExistingProductException(ExistingProductException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<String> handlePermissionDeniedException(PermissionDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        // Extract the root cause of the exception
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        String errorMessage = "Invalid input format";

        String message = mostSpecificCause.getMessage();

        // Check if the error is related to the Role enum
        if (message.contains("com.example.letsplay.model.Role")) {
            errorMessage = "Invalid role value. Accepted values are USER, ADMIN.";
        } else {
            // For other errors, use a more generic error message
            errorMessage = "Error parsing JSON request: " + message;
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getCause().getMessage());
    }

}
