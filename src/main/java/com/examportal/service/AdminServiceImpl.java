package com.examportal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.examportal.custom_exceptions.InvalidCredentialsException;
import com.examportal.custom_exceptions.ResourceAlreadyExistsException;
import com.examportal.custom_exceptions.ResourceNotFoundException;
import com.examportal.dtos.AdminLoginRequest;
import com.examportal.dtos.AdminProfileResponse;
import com.examportal.dtos.ChangePasswordRequest;
import com.examportal.dtos.CreateUserRequest;
import com.examportal.dtos.UpdateAdminProfileRequest;
import com.examportal.dtos.UserResponse;
import com.examportal.entities.Admin;
import com.examportal.entities.Student;
import com.examportal.enums.Role;
import com.examportal.repository.AdminRepository;
import com.examportal.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;
    
    private final StudentRepository studentRepository;
    

	@Override
	public AdminProfileResponse getProfile() {
		// TODO Auto-generated method stub
		
		Admin admin = getCurrentAdmin();
		
		AdminProfileResponse adminProfileResponse = new AdminProfileResponse();
		
		adminProfileResponse.setAdminId(admin.getAdminId());
		adminProfileResponse.setEmail(admin.getEmail());
		adminProfileResponse.setName(admin.getName());
		adminProfileResponse.setRole(admin.getRole());
		
		return adminProfileResponse;
	}

	@Override
	public String updateProfile(UpdateAdminProfileRequest request) {
		// TODO Auto-generated method stub
		
		Admin admin = getCurrentAdmin();
		
		admin.setName(request.getName());

		adminRepository.save(admin);
		
		return "Profile updated successfully.";
	}

	@Override
	public String changePassword(ChangePasswordRequest request) {
		// TODO Auto-generated method stub
		
		Admin admin = getCurrentAdmin();
		
		if (!passwordEncoder.matches(
		        request.getCurrentPassword(),
		        admin.getPassword())) {

		    throw new InvalidCredentialsException("Current password is incorrect.");
		}
		
		if (!request.getNewPassword().equals(request.getConfirmNew())) {
		    throw new InvalidCredentialsException("New passwords do not match.");
		}
		
		admin.setPassword(
		        passwordEncoder.encode(request.getNewPassword()));
		
		adminRepository.save(admin);
		
		return "Password updated successfully.";
	}
	
	private Admin getCurrentAdmin()
	{
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();
		
		return adminRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Admin not Find."));
	}
	
	@Override
	public List<UserResponse> getAllUsers() {

	    List<UserResponse> users = new ArrayList<>();
	    
		 List<Admin> admins = adminRepository.findAll();
			    
			    for( var admin : admins)
			    {
			    	UserResponse response = new UserResponse();
		
			        response.setId(admin.getAdminId());
			        response.setName(admin.getName());
			        response.setEmail(admin.getEmail());
			        response.setRole(admin.getRole());
		
			        users.add(response);
			    }

	    List<Student> students = studentRepository.findAll();
	    
	    for (Student student : students) {

	        UserResponse response = new UserResponse();

	        response.setId(student.getStudentId());
	        response.setName(student.getName());
	        response.setEmail(student.getEmail());
	        response.setRole(student.getRole());

	        users.add(response);
	    }
	    
	   
	    
	    return users;

	}
	
	@Override
	public String createUser(CreateUserRequest request) {

	    // Check if email already exists.
	    if (studentRepository.existsByEmail(request.getEmail())
	            || adminRepository.existsByEmail(request.getEmail())) {

	        throw new ResourceAlreadyExistsException(
	                "User already exists with this email.");
	    }

	    // Generate default password.
	    String defaultPassword =
	            request.getEmail().split("@")[0] + "@123";

	    // Create Student
	    if (request.getRole() == Role.STUDENT) {

	        Student student = new Student();

	        student.setName(request.getName());
	        student.setEmail(request.getEmail());
	        student.setPassword(passwordEncoder.encode(defaultPassword));
	        student.setRole(Role.STUDENT);

	        studentRepository.save(student);

	        return "Student created successfully. Default Password: " + defaultPassword;
	    }

	    // Create Admin
	    Admin admin = new Admin();

	    admin.setName(request.getName());
	    admin.setEmail(request.getEmail());
	    admin.setPassword(passwordEncoder.encode(defaultPassword));
	    admin.setRole(Role.ADMIN);

	    adminRepository.save(admin);

	    return "Admin created successfully. Default Password: " + defaultPassword;
	}
	
	@Override
	public String updateUser(CreateUserRequest request) {

	    Optional<Student> student = studentRepository.findByEmail(request.getEmail());

	    if (student.isPresent()) {

	        Student s = student.get();

	        s.setName(request.getName());

	        studentRepository.save(s);

	        return "Student updated successfully.";
	    }

	    Optional<Admin> admin = adminRepository.findByEmail(request.getEmail());

	    if (admin.isPresent()) {

	        Admin a = admin.get();

	        a.setName(request.getName());

	        adminRepository.save(a);

	        return "Admin updated successfully.";
	    }

	    throw new ResourceNotFoundException("User not found.");
	}
	
	@Override
	public String deleteUser(String email) {

	    Optional<Student> student = studentRepository.findByEmail(email);

	    if (student.isPresent()) {

	        studentRepository.delete(student.get());

	        return "Student deleted successfully.";
	    }

	    Optional<Admin> admin = adminRepository.findByEmail(email);

	    if (admin.isPresent()) {

	        adminRepository.delete(admin.get());

	        return "Admin deleted successfully.";
	    }

	    throw new ResourceNotFoundException("User not found.");
	}

}
