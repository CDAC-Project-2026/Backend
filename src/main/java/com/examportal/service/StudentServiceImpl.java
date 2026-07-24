package com.examportal.service;

import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.dtos.Registration;
import com.examportal.entities.Student;
import com.examportal.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;

	@Override
	public String registerStudent(Registration request) {

		// Prevent duplicate registration using the same email.
		if (studentRepository.existsByEmail(request.getEmail())) {
			throw new ResourceAlreadyExistsException("Student is already registered with this email.");
		}

		// Create a new Student entity from the registration request.
		Student student = new Student();

		student.setName(request.getName());
		student.setEmail(request.getEmail());
		student.setPassword(request.getPassword()); // BCrypt encoding will be added later
		student.setPhone(request.getPhone());
		student.setCity(request.getCity());

		studentRepository.save(student);

		return "Student registered successfully.";
	}

	@Override
	public String loginStudent(String email, String password) {

		// Login logic will be implemented after JWT configuration.
		return "Login API Pending";

	}

}