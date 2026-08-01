package com.examportal.custom_exceptions;

public class ForbiddenException extends RuntimeException{
	
	public ForbiddenException(String message) {
		
		super(message);
		
	}
}
