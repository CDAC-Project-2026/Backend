package com.examportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.dtos.Registration;
import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.StudentDashboardDTO;
import com.examportal.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

 
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
	
	private final StudentService studentservice;
	
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO<String>> register(@Valid @RequestBody Registration request){
		try {
			return ResponseEntity.ok(new ResponseDTO<>(studentservice.registerStudent(request)));
		}catch(ResourceAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO<>("Unsuccessful", null));
		}
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
