package com.examportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	// Find admin using email during login.
	Optional<Admin> findByEmail(String email);

	// Check if an admin with the same email already exists.
	boolean existsByEmail(String email);
}
