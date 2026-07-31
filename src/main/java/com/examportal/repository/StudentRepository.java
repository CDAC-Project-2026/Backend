package com.examportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	// Checks whether a student is already registered with the given email
	boolean existsByEmail(String email);

	// Fetches a student using email
	// Used during login and authentication
	Optional<Student> findByEmail(String email);

}