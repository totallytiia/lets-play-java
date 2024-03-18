package com.example.letsplay.exceptions;

import javax.management.RuntimeErrorException;

public class ExistingProductException extends RuntimeErrorException {

    public ExistingProductException(Error e) {
        super(e);
    }
}
