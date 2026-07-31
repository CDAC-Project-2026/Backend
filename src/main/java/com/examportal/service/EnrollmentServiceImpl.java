package com.examportal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.EnrolledCourseResponse;
import com.examportal.entities.Courses;
import com.examportal.entities.Student;
import com.examportal.entities.StudentEnrolledCourses;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.EnrollmentRepository;
import com.examportal.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
	
	private final StudentRepository studentrepo;
	private final CourseRepository courserepo;
	private final EnrollmentRepository enrollmentrepo;
	
	private Student getCurrentStudent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		return studentrepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Student not found"));
	}
	
	@Override
	public String enrollInCourse(Long CourseId) {
		Student student = getCurrentStudent(); 
		
		Courses course = courserepo.findById(CourseId).orElseThrow(()->new ResourceNotFoundException("Course Not found"));
		
		if(enrollmentrepo.existsByStudent_StudentIdAndCourse_CourseId(student.getStudentId(), CourseId)) {
			throw new ResourceAlreadyExistsException("You are already enrolled in this course");
		}
		
		StudentEnrolledCourses enrollment = new StudentEnrolledCourses();
		enrollment.setStudent(student);
		enrollment.setCourse(course);
		enrollment.setEnrollmentDate(LocalDateTime.now());
		enrollment.setProgress(BigDecimal.ZERO);
		
		enrollmentrepo.save(enrollment);
		
		return "Enrolled Successfully";
	}

	@Override
	public List<EnrolledCourseResponse> getMyEnrollments() {
		Student s = getCurrentStudent();
		
		return enrollmentrepo.findByStudent_StudentId(s.getStudentId())
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}
	
	
	private EnrolledCourseResponse mapToResponse(StudentEnrolledCourses enrollment) {
		EnrolledCourseResponse response = new EnrolledCourseResponse();
		
		response.setEnrollmentId(enrollment.getEnrollmentId());
		response.setCourseId(enrollment.getCourse().getCourseId());
		response.setCourseName(enrollment.getCourse().getCourseName());
		response.setEnrollmentDate(enrollment.getEnrollmentDate());
		response.setDescription(enrollment.getCourse().getDescription());
		response.setProgress(enrollment.getProgress());
		
		return response; 
	}

}










