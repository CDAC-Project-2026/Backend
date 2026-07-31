package com.examportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examportal.entities.Courses;

public interface CourseRepository extends JpaRepository<Courses, Long>{

}
