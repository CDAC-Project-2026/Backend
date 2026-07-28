package com.examportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examportal.dtos.CourseResponse;
import com.examportal.dtos.CreateCourseRequest;
import com.examportal.dtos.ResponseDTO;
import com.examportal.dtos.UpdateCourseRequest;
import com.examportal.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/courses")
@RequiredArgsConstructor
public class CourseController {
	private final CourseService courseservice;
	
	//create new course
	@PostMapping
	public ResponseEntity<ResponseDTO<String>> createCourse(@Valid @RequestBody CreateCourseRequest req){
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("Success",courseservice.createCourse(req)));
	}
	
	//to get the list of all courses
	@GetMapping
	public ResponseEntity<ResponseDTO<List<CourseResponse>>> getAllCourses(){
		return ResponseEntity.ok(new ResponseDTO<>("success", courseservice.getAllCourses()));
	}
	
	//to get theo course by id
	@GetMapping("/{courseId}")
	public ResponseEntity<ResponseDTO<CourseResponse>> getCourseById(@PathVariable Long courseId){
		
		return ResponseEntity.ok(new ResponseDTO<>("success", courseservice.getCourseById(courseId)));
	}
	
	//to update the course
	@PutMapping("/{courseId}")
	public ResponseEntity<ResponseDTO<String>> updateCourse(@PathVariable Long courseId, @Valid @RequestBody UpdateCourseRequest req){
		return ResponseEntity.ok(new ResponseDTO<>("Success",courseservice.updateCourse(courseId, req)));
	}
	
	//delete the course
	@DeleteMapping("/{courseId}")
	public ResponseEntity<ResponseDTO<String>> deleteCourse(@PathVariable Long courseId){
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("Success",courseservice.deleteCourse(courseId)));
	}
}
