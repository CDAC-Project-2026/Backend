package com.examportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	private final StudentService studentService;

	// Registers a new student after validating the request body.
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO<String>> registerStudent(
			@Valid @RequestBody Registration request) {

		try {

			String message = studentService.registerStudent(request);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResponseDTO<>("Success", message));

		} catch (ResourceAlreadyExistsException e) {

			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ResponseDTO<>("Failed", e.getMessage()));

		}

	}

}