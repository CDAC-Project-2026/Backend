package com.examportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.dtos.Registration;
import com.examportal.dtos.ResponseDTO;
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
	
}
