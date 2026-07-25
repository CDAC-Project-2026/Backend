package com.examportal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.examportal.entities.Student;
import com.examportal.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final StudentRepository studentRepository;

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {

		// Search the student using the email stored inside the JWT.
		Student student = studentRepository.findByEmail(email)
				.orElseThrow(() ->
						new UsernameNotFoundException("Student not found"));

		// Convert Student entity into Spring Security's UserDetails object.
		return new CustomUserDetails(student);
	}
}