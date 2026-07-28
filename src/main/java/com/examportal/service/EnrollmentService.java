package com.examportal.service;

import java.util.List;

import com.examportal.dtos.EnrolledCourseResponse;

public interface EnrollmentService {
	String enrollInCourse(Long CourseId);
	
	List<EnrolledCourseResponse> getMyEnrollments();
}
