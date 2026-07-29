package com.examportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.StudentEnrolledCourses;

public interface EnrollmentRepository extends JpaRepository<StudentEnrolledCourses, Long>{
	List<StudentEnrolledCourses> findByStudent_StudentId(Long StudentId);
	
	boolean existsByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
}
