package com.examportal.service;

import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.dtos.Registration;
import com.examportal.entities.Student;
import com.examportal.repository.StudentRepository;
import com.examportal.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.examportal.custom_exceptions.InvalidCredentialsException;
import com.examportal.dtos.LoginRequest;
import com.examportal.dtos.LoginResponse;

import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

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
		// Encrypt the password before storing it in the database.
		student.setPassword(passwordEncoder.encode(request.getPassword()));
		student.setPhone(request.getPhone());
		student.setCity(request.getCity());

		studentRepository.save(student);

		return "Student registered successfully.";
	}

	@Override
	public LoginResponse loginStudent(LoginRequest request) 
	{

		// Find the student using the email entered during login.
		Optional<Student> optionalStudent =
				studentRepository.findByEmail(request.getEmail());

		// Email not found in the database.
		if (optionalStudent.isEmpty()) 
		{
			throw new InvalidCredentialsException("Invalid email or password.");
		}

		Student student = optionalStudent.get();

		// Compare the entered password with the encrypted password stored in the database.
		if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) 
		{
			throw new InvalidCredentialsException("Invalid email or password.");
		}

		// Generate JWT after successful authentication.
		String token = jwtService.generateToken(student.getEmail());

		// Return login response containing the JWT.
		return new LoginResponse(
				token,
				"Bearer",
				"Login successful."
		);
	}

}