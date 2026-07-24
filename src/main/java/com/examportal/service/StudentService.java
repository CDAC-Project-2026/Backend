package com.examportal.service;

import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.Registration;

public interface StudentService {

	// Registers a new student after validating the request
	String registerStudent(Registration request);

	// Authenticates a student using email and password
	// JWT token generation will be added in a later phase
	String loginStudent(LoginRequest request);

}