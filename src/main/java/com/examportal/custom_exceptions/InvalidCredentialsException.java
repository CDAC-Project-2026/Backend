package com.examportal.custom_exceptions;

// Thrown when the email or password entered during login is incorrect.
public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException(String message) {
		super(message);
	}

}