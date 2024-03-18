package com.example.letsplay.exceptions;

import javax.management.RuntimeErrorException;

public class DataNotValidatedException extends RuntimeErrorException{
	public DataNotValidatedException(Error e) {
		super(e);
	}

}
