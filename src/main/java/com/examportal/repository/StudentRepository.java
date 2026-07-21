package com.examportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
	boolean existsByEmail(String email);
}
