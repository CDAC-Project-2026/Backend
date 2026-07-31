package com.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.StudentEnrolledCourses;

public interface StudentEnrolledCoursesRepository extends JpaRepository<StudentEnrolledCourses, Long>{

	boolean existsByStudentStudentIdAndCourseCourseId(Long studentId, Long courseId);
}
