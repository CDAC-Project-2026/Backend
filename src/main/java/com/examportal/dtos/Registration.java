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
	@NotBlank
	private String name;
	
	@Email
	private String email;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String phone;
	
	@NotBlank
	private String city;
}
