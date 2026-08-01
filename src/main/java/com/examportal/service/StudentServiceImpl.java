package com.examportal.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.custom_exceptions.*;

import com.examportal.dtos.NotificationDTO;
import com.examportal.dtos.ChangePasswordRequest;
import com.examportal.dtos.DeleteAccountRequest;
import com.examportal.dtos.Registration;
import com.examportal.dtos.StudentDashboardDTO;
import com.examportal.dtos.StudentProfileResponse;
import com.examportal.dtos.UpdateStudentProfileRequest;

import com.examportal.entities.Student;

import com.examportal.enums.Role;

import com.examportal.repository.StudentRepository;
import com.examportal.repository.NotificationRepository;
import com.examportal.repository.StudentTestsRepository;

import com.examportal.security.JwtService;

import com.examportal.utils.AuthUtil;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;




@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
    private final StudentTestsRepository studentTestsRepo;
    private final NotificationRepository notificationRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	private final AuthUtil authUtil;

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
		
		// Every newly registered user is assigned the STUDENT role.
		student.setRole(Role.STUDENT);
		
		student.setCity(request.getCity());

		studentRepository.save(student);

		return "Student registered successfully.";
	}
	
	@Override
	public StudentProfileResponse getProfile() {

		// Fetch the student from the database.
		Student student = authUtil.getCurrentStudent();

		// Convert entity to response DTO.
		StudentProfileResponse response = new StudentProfileResponse();

		response.setStudentId(student.getStudentId());
		response.setName(student.getName());
		response.setEmail(student.getEmail());
		response.setPhone(student.getPhone());
		response.setCity(student.getCity());
		response.setRank(student.getStudentRank());

		return response;
	}
	
	
	
	@Override
	public String updateProfile(UpdateStudentProfileRequest request) {

		Student student = authUtil.getCurrentStudent();

		student.setName(request.getName());
		student.setPhone(request.getPhone());
		student.setCity(request.getCity());

		studentRepository.save(student);

		return "Profile updated successfully.";
	}

	@Override
	public String changePassword(ChangePasswordRequest request) {
		Student student = authUtil.getCurrentStudent();
		if (!passwordEncoder.matches(
		        request.getCurrentPassword(),
		        student.getPassword())) {

		    throw new InvalidCredentialsException("Current password is incorrect.");
		}
		
		if (!request.getNewPassword().equals(request.getConfirmNew())) {
		    throw new IllegalArgumentException("New passwords do not match.");
		}
		
		student.setPassword(
		        passwordEncoder.encode(request.getNewPassword()));
		
		studentRepository.save(student);
		
		return "Password updated successfully.";
		
	}
	
	@Override
	public String deleteAccount(DeleteAccountRequest request) {

	    Authentication authentication =
	            SecurityContextHolder.getContext().getAuthentication();

	    String email = authentication.getName();

	    Student student = studentRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Student not found"));

	    if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
	        throw new RuntimeException("Incorrect password.");
	    }

	    studentRepository.delete(student);

	    return "Account deleted successfully.";
	}
  
  
  @Override
	public StudentDashboardDTO getDashboard() {
	  
		Student student = authUtil.getCurrentStudent();

	    List<BigDecimal> recentScores = studentTestsRepo.findRecentScores(student.getStudentId(), PageRequest.of(0, 4));
	    List<NotificationDTO> notifications = notificationRepo.findNotificationsForStudent(student.getStudentId());

	    return new StudentDashboardDTO(
	            student.getName(),
	            student.getStudentRank(),
	            recentScores,
	            notifications
	    );
	}

	

}
