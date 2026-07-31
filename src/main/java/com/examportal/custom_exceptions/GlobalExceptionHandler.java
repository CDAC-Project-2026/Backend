package com.examportal.custom_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.examportal.dtos.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handles duplicate resource exceptions across the application.
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ResponseDTO<String>> handleResourceAlreadyExists(
			ResourceAlreadyExistsException ex) {

		ResponseDTO<String> response =
				new ResponseDTO<>("Failed", ex.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	// Handles invalid login attempts.
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ResponseDTO<String>> handleInvalidCredentials(
			InvalidCredentialsException ex) {

		ResponseDTO<String> response =
				new ResponseDTO<>("Failed", ex.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	//misssing resource lookups
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseDTO<String>> handleResourceNotFound(ResourceNotFoundException e){
		ResponseDTO<String> response = new ResponseDTO<>("Failed",e.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}