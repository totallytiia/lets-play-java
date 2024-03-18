package com.example.letsplay.exceptions;

import javax.management.RuntimeErrorException;

public class PermissionDeniedException extends RuntimeErrorException {
    public PermissionDeniedException(Error message) {
        super(message);
    }
}
