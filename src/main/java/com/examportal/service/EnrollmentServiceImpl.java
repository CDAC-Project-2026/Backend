package com.examportal.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.EnrolledCourseResponse;
import com.examportal.dtos.EnrolledStudentResponse;
import com.examportal.entities.Courses;
import com.examportal.entities.Student;
import com.examportal.entities.StudentEnrolledCourses;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.EnrollmentRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.StudentTestsRepository;
import com.examportal.repository.TestRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
	
	private final StudentRepository studentrepo;
	private final CourseRepository courserepo;
	private final EnrollmentRepository enrollmentrepo;
	private final TestRepository testRepo;
	private final StudentTestsRepository studentTestsRepo;
	
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

	@Override
	public List<EnrolledStudentResponse> getEnrollmentForCourse(Long courseId) {
		courserepo.findById(courseId).orElseThrow(()->new ResourceNotFoundException("Course Not Found"));
		
		return enrollmentrepo.findByCourse_CourseId(courseId)
				.stream()
				.map(this::mapToStudentResponse)
				.collect(Collectors.toList());
	}
	
	
	private BigDecimal calculateProgress(Long studentId, Long courseId) {
		long totalTests = testRepo.countByCoursesCourseIdAndDraftFalse(courseId);
		if(totalTests==0) {
			return BigDecimal.ZERO;
		}
		
		long attemptedTestCount=studentTestsRepo.countAttemptedTestsByStudentAndCourse(studentId, courseId);
		
		return BigDecimal.valueOf(attemptedTestCount)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(totalTests), 0, RoundingMode.HALF_UP);
	}
	
	
	private EnrolledCourseResponse mapToResponse(StudentEnrolledCourses enrollment) {
		EnrolledCourseResponse response = new EnrolledCourseResponse();
		
		Long studentId = enrollment.getStudent().getStudentId();
		Long courseId = enrollment.getCourse().getCourseId();
		
		response.setEnrollmentId(enrollment.getEnrollmentId());
		response.setCourseId(enrollment.getCourse().getCourseId());
		response.setCourseName(enrollment.getCourse().getCourseName());
		response.setEnrollmentDate(enrollment.getEnrollmentDate());
		response.setDescription(enrollment.getCourse().getDescription());
		response.setProgress(calculateProgress(studentId, courseId));
		
		return response; 
	}
	
	private EnrolledStudentResponse mapToStudentResponse(StudentEnrolledCourses enrollment) {
		EnrolledStudentResponse response = new EnrolledStudentResponse();
		
		Long studentId = enrollment.getStudent().getStudentId();
		Long courseId = enrollment.getCourse().getCourseId();
		
		response.setStudentId(enrollment.getStudent().getStudentId());
		response.setName(enrollment.getStudent().getName());
		response.setEmail(enrollment.getStudent().getEmail());
		response.setProgress(calculateProgress(studentId, courseId));
		
		return response;
	}

}










