package com.examportal.service;

import java.util.List;

import com.examportal.dtos.CourseResponse;
import com.examportal.dtos.CreateCourseRequest;
import com.examportal.dtos.UpdateCourseRequest;

public interface CourseService {
	
	String createCourse(CreateCourseRequest request);
	
	String updateCourse(Long courseId, UpdateCourseRequest request);
	
	String deleteCourse(Long courseId);
	
	CourseResponse getCourseById(Long courseId);
	
	List<CourseResponse> getAllCourses();
	
	
}
