package com.examportal.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.entities.Student;
import com.examportal.repository.StudentRepository;


@Component
public class AuthUtil {
	
	private final StudentRepository studentRepository;
	
	public AuthUtil(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}
	
	 public Student getCurrentStudent() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        return studentRepository.findByEmail(email)
	                .orElseThrow(() -> new ResourceNotFoundException("Student not found."));
	    }
	 
	 
	 public Long getCurrentStudentId() {
	        return getCurrentStudent().getStudentId();
	    }
}
