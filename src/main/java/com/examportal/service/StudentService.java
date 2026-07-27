package com.examportal.service;


import com.examportal.dtos.ChangePasswordRequest;
import com.examportal.dtos.Registration;
import com.examportal.dtos.StudentProfileResponse;
import com.examportal.dtos.UpdateStudentProfileRequest;

public interface StudentService {

	// Registers a new student after validating the request
	String registerStudent(Registration request);

	StudentProfileResponse getProfile();
	
	String updateProfile(UpdateStudentProfileRequest request);
	
	String changePassword(ChangePasswordRequest request);
	
}