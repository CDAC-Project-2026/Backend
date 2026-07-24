package com.examportal.custom_exceptions;

// Thrown when a user tries to create a resource
// that already exists in the system.
public class ResourceAlreadyExistsException extends RuntimeException {

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}

}