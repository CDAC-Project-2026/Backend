package com.examportal.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.SubmitTestDTO;
import com.examportal.entities.Courses;
import com.examportal.entities.Student;
import com.examportal.entities.StudentActivityLog;
import com.examportal.entities.Test;
import com.examportal.repository.CourseRepository;
import com.examportal.repository.StudentActivityLogRepository;
import com.examportal.repository.StudentRepository;
import com.examportal.repository.TestRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class StudentActivityLoggingAspect {
	
	private final StudentActivityLogRepository logRepo;
	private final StudentRepository studentRepo;
	private final TestRepository testRepo;
	private final CourseRepository courseRepo;
	
	@AfterReturning(
            pointcut = "execution(* com.examportal.service.StudentTestsServiceImpl.submitTest(..)) && args(request)",
            returning = "result")
    public void logTestAttempt(SubmitTestDTO request, Object result) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepo.findByEmail(email).orElse(null);
        if (student == null) return;

        String testName = testRepo.findById(request.getTestId())
                .map(Test::getTestName)
                .orElse("a test");

        StudentActivityLog log = new StudentActivityLog();
        log.setDescription(student.getName() + " attempted " + testName + ".");
        log.setLogTime(LocalDateTime.now());
        logRepo.save(log);
    }
	
	
	@AfterReturning(
	        pointcut = "execution(* com.examportal.service.EnrollmentServiceImpl.enrollInCourse(..)) && args(courseId)",
	        returning = "result")
	public void logEnrollment(Long courseId, Object result) {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Student student = studentRepo.findByEmail(email).orElse(null);
	    if (student == null) return;

	    String courseName = courseRepo.findById(courseId)
	            .map(Courses::getCourseName)
	            .orElse("a course");

	    StudentActivityLog log = new StudentActivityLog();
	    log.setDescription(student.getName() + " enrolled in " + courseName + ".");
	    log.setLogTime(LocalDateTime.now());
	    logRepo.save(log);
	}
	
	
	@AfterReturning(
	        pointcut = "execution(* com.examportal.service.AuthenticationServiceImpl.login(..)) && args(request)",
	        returning = "result")
	public void logStudentLogin(LoginRequest request, Object result) {
	    Student student = studentRepo.findByEmail(request.getEmail()).orElse(null);
	    if (student == null) return; // not a student — likely an admin login, skip logging

	    StudentActivityLog log = new StudentActivityLog();
	    log.setDescription(student.getName() + " logged in.");
	    log.setLogTime(LocalDateTime.now());
	    logRepo.save(log);
	}
}
