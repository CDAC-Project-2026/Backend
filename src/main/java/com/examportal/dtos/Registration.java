package com.examportal.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Registration {

	// Student's full name
	@NotBlank(message = "Name is required")
	private String name;

	// Email is used as the unique login ID.
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	private String email;

	// Password will be encrypted using BCrypt before storing.
	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Phone number is required")
	private String phone;

	@NotBlank(message = "City is required")
	private String city;

}