package com.examportal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.examportal.entities.Student;
import com.examportal.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

import com.examportal.entities.Admin;
import com.examportal.repository.AdminRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final StudentRepository studentRepository;
	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String email)
	        throws UsernameNotFoundException {

	    // First check if the email belongs to a student.
	    Student student = studentRepository.findByEmail(email).orElse(null);

	    if (student != null) {
	        return new CustomUserDetails(student);
	    }

	    // If not a student, check if it belongs to an admin.
	    Admin admin = adminRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new UsernameNotFoundException("User not found"));

	    return new CustomUserDetails(admin);
	}
}