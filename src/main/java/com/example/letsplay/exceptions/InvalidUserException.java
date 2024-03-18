package com.example.letsplay.exceptions;

import javax.management.RuntimeErrorException;

public class InvalidUserException extends RuntimeErrorException {

	public InvalidUserException(Error e) {
		super(e);
	}

}
