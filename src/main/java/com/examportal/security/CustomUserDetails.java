package com.examportal.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.examportal.entities.Student;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	// Student entity received after successful lookup.
	private final Student student;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		// Roles will be added later.
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {

		return student.getPassword();
	}

	@Override
	public String getUsername() {

		// Email is used as the login username.
		return student.getEmail();
	}

}