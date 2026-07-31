package com.examportal.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	// Email used to identify the student during login.
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	private String email;

	// Password entered by the student.
	@NotBlank(message = "Password is required")
	private String password;

}