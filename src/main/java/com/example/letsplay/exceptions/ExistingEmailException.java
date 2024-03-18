package com.example.letsplay.exceptions;

import javax.management.RuntimeErrorException;

public class ExistingEmailException extends RuntimeErrorException{
	public ExistingEmailException(Error e) {
		super(e);
	}

}
