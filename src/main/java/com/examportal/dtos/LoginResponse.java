package com.examportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

	// JWT token generated after successful login.
	private String token;

	// Token type used in the Authorization header.
	private String tokenType;

	// Success message returned to the client.
	private String message;

}