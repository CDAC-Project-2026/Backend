package com.examportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.dtos.EnrolledCourseResponse;
import com.examportal.dtos.ResponseDTO;
import com.examportal.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class EnrollmentController {
	private final EnrollmentService enrollmentService;
	
	@PostMapping("/courses/{courseId}/enroll")
	public ResponseEntity<ResponseDTO<String>> enrollInCourse(@PathVariable Long courseId){
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("Success",enrollmentService.enrollInCourse(courseId)));
	}
	
	@GetMapping("/my-courses")
	public ResponseEntity<ResponseDTO<List<EnrolledCourseResponse>>> getMyCourses(){
		return ResponseEntity.ok(new ResponseDTO<>("Success",enrollmentService.getMyEnrollments()));
	}
 }
