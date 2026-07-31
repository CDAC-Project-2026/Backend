package com.examportal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.examportal.entities.Admin;
import com.examportal.entities.Student;
import com.examportal.enums.Role;

public class CustomUserDetails implements UserDetails {

	// Login email of the authenticated user.
	private final String email;

	// Encrypted password stored in the database.
	private final String password;

	// Role assigned to the user.
	private final Role role;

	// Create UserDetails from a Student entity.
	public CustomUserDetails(Student student) {
		this.email = student.getEmail();
		this.password = student.getPassword();
		this.role = student.getRole();
	}

	// Create UserDetails from an Admin entity.
	public CustomUserDetails(Admin admin) {
		this.email = admin.getEmail();
		this.password = admin.getPassword();
		this.role = admin.getRole();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		// Convert the role into a Spring Security authority.
		return List.of(
				new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {

		// Email is used as the login username.
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}