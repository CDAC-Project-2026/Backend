package com.examportal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.dtos.CourseResponse;
import com.examportal.dtos.ResponseDTO;
import com.examportal.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student/courses")
@RequiredArgsConstructor
public class StudentCourseController {
	
	private final CourseService courseservice;
	
	@GetMapping
	public ResponseEntity<ResponseDTO<List<CourseResponse>>> getAllCourses(){
		return ResponseEntity.ok(new ResponseDTO<>("Success",courseservice.getAllCourses()));
	}
	
	@GetMapping("/{courseId}")
	public ResponseEntity<ResponseDTO<CourseResponse>> getCourseById(@PathVariable Long courseId){
		return ResponseEntity.ok(new ResponseDTO<>("success", courseservice.getCourseById(courseId)));
	}
}
