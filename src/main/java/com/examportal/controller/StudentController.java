package com.examportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;

import com.examportal.dtos.Registration;
import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.StudentProfileResponse;
import com.examportal.dtos.UpdateStudentProfileRequest;
import com.examportal.dtos.ChangePasswordRequest;
import com.examportal.dtos.DeleteAccountRequest;
import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.LoginResponse;
import com.examportal.dtos.StudentDashboardDTO;

import com.examportal.service.AuthenticationService;
import com.examportal.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController 
{

	private final StudentService studentService;
	private final AuthenticationService authenticationService;

	// Registers a new student after validating the request body
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO<String>> registerStudent(
			@Valid @RequestBody Registration request) 
	{

		String message = studentService.registerStudent(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDTO<>("Success", message));
	}
	
	// Authenticates a student using email and password
	@PostMapping("/login")
	public ResponseEntity<ResponseDTO<LoginResponse>> login(
	        @Valid @RequestBody LoginRequest request) {

	    return ResponseEntity.ok(
	            new ResponseDTO<>("Success",
	                    authenticationService.login(request)));
	}
	
	// Used to verify that JWT authentication is working i.e  TESTING AAHE...!!!  
	@GetMapping("/profile")
	public ResponseEntity<ResponseDTO<StudentProfileResponse>> getProfile() {

		return ResponseEntity.ok(
				new ResponseDTO<>(
						"Success",
						studentService.getProfile()));
	}
	
	@PutMapping("/profile")
	public ResponseEntity<ResponseDTO<String>> updateProfile(
			@Valid @RequestBody UpdateStudentProfileRequest request) {

		return ResponseEntity.ok(
				new ResponseDTO<>(
						"Success",
						studentService.updateProfile(request)));
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<ResponseDTO<String>> changePassword(
	        @Valid @RequestBody ChangePasswordRequest request) {

	    return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentService.changePassword(request)));
	}
	
	@DeleteMapping("/account")
	public ResponseEntity<ResponseDTO<String>> deleteAccount(
	        @RequestBody DeleteAccountRequest request) {

	    return ResponseEntity.ok(
	            new ResponseDTO<>(
	                    "Success",
	                    studentService.deleteAccount(request)
	            )
	    );
	}
  
  @GetMapping("/{studentId}/dashboard")
	public ResponseEntity<?> getDashboard(@PathVariable Long studentId){
		try {
			StudentDashboardDTO dashboardDTO = studentservice.getDashboard(studentId);
			return ResponseEntity.status(HttpStatus.OK).body(dashboardDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student Dashboard Not Found");
		}
	}
  
  
}
