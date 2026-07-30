package com.examportal.service;

import java.util.List;

import com.examportal.dtos.EnrolledCourseResponse;
import com.examportal.dtos.EnrolledStudentResponse;

public interface EnrollmentService {
	String enrollInCourse(Long CourseId);
	
	List<EnrolledCourseResponse> getMyEnrollments();
	
	List<EnrolledStudentResponse> getEnrollmentForCourse(Long courseId);
}
